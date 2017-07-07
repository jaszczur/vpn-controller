package com.example.vpncontroller.modules.vpnconnection.impl

import com.example.vpncontroller.domain.ConnectionPerformanceMetric
import com.example.vpncontroller.domain.Protocol
import com.example.vpncontroller.domain.ServerId
import com.example.vpncontroller.modules.countries.Countries
import com.example.vpncontroller.modules.vpnconnection.VpnConnection
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
@Qualifier("openvpn")
class SystemdOpenvpnConnection(countries: Countries) : VpnConnection {
    private val conversions = SystemdOpenvpnConversions(countries)

    override fun enable(id: ServerId, protocol: Protocol): Mono<ServerId> {
        SystemdOpenvpnCommand("start", conversions.unitInstance(id, protocol))
//                .executeGettingOutput()
        TODO()
    }

    override fun active(): Mono<ServerId> {
        return SystemdCommand("list-units", "--state=active", "--plain", "--no-legend", "openvpn-client*")
                .executeGettingOutput()
                .single()
                .flatMap(conversions::extractUnitInstance)
                .flatMap(conversions::unitInstanceToServerId)
    }

    override fun disable(): Mono<ServerId> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun monitor(): Flux<ConnectionPerformanceMetric> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

open class SystemdCommand(vararg params: String) : Command(systemctlCmd, *params) {
    companion object {
        val systemctlCmd = "systemctl"
    }
}

open class SystemdOpenvpnCommand(val action: String, vpnName:String)
    : SystemdCommand(action, unitName(vpnName)) {
    companion object {
        fun unitName(vpnName: String) = "openvpn-client@$vpnName.service"
    }
}