package com.github.jaszczur.vpncontroller.modules.vpnconnection

import com.github.jaszczur.vpncontroller.domain.ConnectionPerformanceMetric
import reactor.core.publisher.Flux

interface Monitoring {
    fun monitor(): Flux<ConnectionPerformanceMetric>
}