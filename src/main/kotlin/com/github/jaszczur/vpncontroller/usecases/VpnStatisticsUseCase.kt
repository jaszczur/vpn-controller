package com.github.jaszczur.vpncontroller.usecases

import com.github.jaszczur.vpncontroller.domain.VpnServerStats
import com.github.jaszczur.vpncontroller.modules.countries.Countries
import com.github.jaszczur.vpncontroller.modules.stats.VpnStatsAdapter
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class VpnStatisticsUseCase(vpnStatsRest: VpnStatsAdapter,
                           private val countries: Countries) {
    private val vpnStatistics = VpnStatistics(vpnStatsRest)

    fun serverStats(countryCode: String): Flux<VpnServerStats> =
            countries.fuzzyByCode(countryCode)
                    .flatMapMany(vpnStatistics::serverStats)

    fun sortedStats(countryCode: String): Flux<VpnServerStats> =
            countries.fuzzyByCode(countryCode)
                    .flatMapMany(vpnStatistics::sortedStats)

    fun findBest(countryCode: String): Mono<VpnServerStats> =
            countries.fuzzyByCode(countryCode)
                    .flatMap(vpnStatistics::findBest)


}

