package com.example.vpncontroller.boundary.api

import com.example.vpncontroller.domain.Country
import com.example.vpncontroller.domain.VpnServerStats
import reactor.core.publisher.Flux

interface VpnStatsAdapter {
    fun serverStats(country: Country): Flux<VpnServerStats>
}