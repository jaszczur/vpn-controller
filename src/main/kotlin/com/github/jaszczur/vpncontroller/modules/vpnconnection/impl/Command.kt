package com.github.jaszczur.vpncontroller.modules.vpnconnection.impl

import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit
import java.util.function.Consumer
import java.util.function.Function


open class Command(val command: List<String>) {
    constructor(command: String, vararg params: String)
            : this(listOf(command) + params.toList())

    fun executeGettingOutput(): Flux<String> =
            Flux.using(resourceSupplier, sourceSupplier, resourceCleanup)
                    .subscribeOn(Schedulers.elastic())

    private val resourceSupplier = Callable {
        val proc = createProcess().start()
        Pair(proc, BufferedReader(InputStreamReader(proc.inputStream)))
    }

    private val sourceSupplier = Function<Pair<Process, BufferedReader>, Flux<String>> { (_, reader) ->
        Flux.fromStream(reader.lines())
    }

    private val resourceCleanup = Consumer<Pair<Process, BufferedReader>> { (proc, reader) ->
        reader.close()
        proc.waitFor(1, TimeUnit.SECONDS)
        val exitCode = proc.exitValue()
        if (exitCode != 0)
            throw IllegalStateException("Process failed. Exit code = $exitCode")
    }

    private fun createProcess() = ProcessBuilder().command(command).redirectErrorStream(true)
}
