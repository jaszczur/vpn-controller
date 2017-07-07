package com.github.jaszczur.vpncontroller.modules.stats

import com.github.jaszczur.vpncontroller.domain.Country
import com.github.jaszczur.vpncontroller.domain.VpnServerStats
import reactor.core.publisher.Flux

interface VpnStatsAdapter {
    fun serverStats(country: Country): Flux<VpnServerStats>
}