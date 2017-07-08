package com.github.jaszczur.vpncontroller.modules.vpnconnection

import com.github.jaszczur.vpncontroller.domain.ConnectableServer
import reactor.core.publisher.Mono

interface VpnConnection {
    fun enable(server: ConnectableServer): Mono<ConnectableServer>
    fun active(): Mono<ConnectableServer>
    fun disable(): Mono<ConnectableServer>
}
