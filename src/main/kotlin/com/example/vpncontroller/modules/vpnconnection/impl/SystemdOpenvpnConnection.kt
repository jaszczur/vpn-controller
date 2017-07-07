package com.example.vpncontroller.modules.vpnconnection.impl

import com.example.vpncontroller.domain.ConnectionPerformanceMetric
import com.example.vpncontroller.domain.ServerId
import com.example.vpncontroller.modules.vpnconnection.VpnConnection
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class SystemdOpenvpnConnection : VpnConnection {
    override fun enable(id: ServerId): Mono<ServerId> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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