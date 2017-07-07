package com.github.jaszczur.vpncontroller.usecases

import com.github.jaszczur.vpncontroller.domain.VpnServerStats
import com.github.jaszczur.vpncontroller.modules.countries.Countries
import com.github.jaszczur.vpncontroller.modules.stats.VpnStatsAdapter
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class VpnStatisticsUseCase(private val vpnStatsRest: VpnStatsAdapter,
                           private val countries: Countries) {

    fun serverStats(countryCode: String): Flux<VpnServerStats> =
            findCountry(countryCode)
                    .switchIfEmpty(Mono.error(CountryNotFoundException(countryCode)))
                    .flatMapMany(vpnStatsRest::serverStats)


    fun sortedStats(countryCode: String): Flux<VpnServerStats> =
            serverStats(countryCode)
                    .sort(compareBy { it.networkLoad })

    fun findBest(countryCode: String): Mono<VpnServerStats> =
            serverStats(countryCode)
                    .collectSortedList(compareBy { it.networkLoad })
                    .map { it.firstOrNull() }

    private fun findCountry(code: String) = Mono.justOrEmpty(countries.byCode(code.toUpperCase()))

}