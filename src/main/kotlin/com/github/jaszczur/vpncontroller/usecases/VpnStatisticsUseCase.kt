package com.github.jaszczur.vpncontroller.usecases

import com.github.jaszczur.vpncontroller.domain.Country
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
            findCountry(countryCode)
                    .flatMapMany(vpnStatistics::serverStats)

    fun sortedStats(countryCode: String): Flux<VpnServerStats> =
            findCountry(countryCode)
                    .flatMapMany(vpnStatistics::sortedStats)

    fun findBest(countryCode: String): Mono<VpnServerStats> =
            findCountry(countryCode)
                    .flatMap(vpnStatistics::findBest)

    private fun findCountry(code: String) =
            Mono.justOrEmpty(countries.byCode(code.toUpperCase()))
                    .switchIfEmpty(Mono.error(CountryNotFoundException(code)))

}

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