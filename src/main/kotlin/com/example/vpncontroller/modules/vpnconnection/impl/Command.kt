package com.example.vpncontroller.modules.vpnconnection.impl

import reactor.core.publisher.Flux
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.Callable
import java.util.function.Consumer
import java.util.function.Function


open class Command(val command: List<String>) {
    constructor(command: String, vararg params: String)
            : this(listOf(command) + params.toList())

    fun executeGettingOutput(): Flux<String> {
        val resourceSupplier = Callable {
            val proc = createProcess().start()
            BufferedReader(InputStreamReader(proc.inputStream))
        }

        val sourceSupplier = Function { reader: BufferedReader ->
            Flux.fromStream(reader.lines())
        }

        val resourceCleanup = Consumer<BufferedReader> { it.close() }

        return Flux.using(resourceSupplier, sourceSupplier, resourceCleanup)
    }

    private fun createProcess() = ProcessBuilder().command(command)
}
