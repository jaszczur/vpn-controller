package com.github.jaszczur.vpncontroller.modules.vpnconnection

import com.github.jaszczur.vpncontroller.domain.Protocol
import com.github.jaszczur.vpncontroller.domain.ServerId
import reactor.core.publisher.Mono

interface VpnConnection {
    fun enable(id: ServerId, protocol: Protocol): Mono<ServerId>
    fun active(): Mono<ServerId>
    fun disable(): Mono<ServerId>
}
