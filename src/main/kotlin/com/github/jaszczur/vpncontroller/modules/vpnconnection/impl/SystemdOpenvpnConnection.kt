package com.github.jaszczur.vpncontroller.modules.vpnconnection.impl

import com.github.jaszczur.vpncontroller.domain.ConnectableServer
import com.github.jaszczur.vpncontroller.modules.countries.Countries
import com.github.jaszczur.vpncontroller.modules.vpnconnection.VpnConnection
import com.github.jaszczur.vpncontroller.modules.vpnconnection.impl.adapters.OpenvpnServerUnitConverter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
@Qualifier("openvpn")
class SystemdOpenvpnConnection(countries: Countries) : VpnConnection {
    private val converter = OpenvpnServerUnitConverter(countries)

    override fun active(): Mono<ConnectableServer> =
            SystemdCommand("list-units", "--state=active", "--plain", "--no-legend", "openvpn-client*")
                    .executeGettingOutput()
                    .single()
                    .flatMap { Mono.justOrEmpty(converter.fromUnitListLine(it)) }

    override fun enable(server: ConnectableServer): Mono<ConnectableServer> =
            disable().then(startServer(server))

    override fun disable(): Mono<ConnectableServer> =
            active().flatMap(this::stopServer)

    private fun startServer(server: ConnectableServer) =
            SystemUnitCommand("start", converter.toUnitName(server))
                    .executeCheckingForFailures()
                    .map { server }

    private fun stopServer(serverToBeDisabled: ConnectableServer) =
            SystemUnitCommand("stop", converter.toUnitName(serverToBeDisabled))
                    .executeCheckingForFailures()
                    .map { serverToBeDisabled }
}

open class SystemdCommand(vararg params: String) : Command(systemctlCmd, *params) {
    companion object {
        val systemctlCmd = "systemctl"
    }

    fun executeCheckingForFailures(): Mono<List<String>> =
            executeGettingOutput()
                    .collectList()
                    .map {
                        // TODO: determine failure in more elegant way
                        val failed = it.filter { it.contains("Failed") }.size > 1
                        if (failed)
                            throw IllegalStateException("Failed to stop service: $it")
                        else
                            it
                    }
}

open class SystemUnitCommand(action: String, unit: String)
    : SystemdCommand(action, unit)