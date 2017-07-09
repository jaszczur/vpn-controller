package com.github.jaszczur.vpncontroller.usecases

import com.github.jaszczur.vpncontroller.domain.Country
import com.github.jaszczur.vpncontroller.domain.VpnServerStats
import com.github.jaszczur.vpncontroller.modules.stats.VpnStatsAdapter
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class VpnStatistics(private val vpnStatsRest: VpnStatsAdapter) {
    fun serverStats(country: Country): Flux<VpnServerStats> =
            vpnStatsRest.serverStats(country)


    fun sortedStats(country: Country): Flux<VpnServerStats> =
            serverStats(country)
                    .sort(compareBy { it.networkLoad })

    fun findBest(country: Country): Mono<VpnServerStats> =
            serverStats(country)
                    .collectSortedList(compareBy { it.networkLoad })
                    .map { it.firstOrNull() }
}