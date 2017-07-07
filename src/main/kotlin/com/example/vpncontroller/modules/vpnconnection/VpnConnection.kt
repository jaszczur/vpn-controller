package com.example.vpncontroller.modules.vpnconnection

import com.example.vpncontroller.domain.ConnectionPerformanceMetric
import com.example.vpncontroller.domain.ServerId
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface VpnConnection {
    fun enable(id: ServerId): Mono<ServerId>
    fun disable(): Mono<ServerId>
    fun monitor(): Flux<ConnectionPerformanceMetric>
}
