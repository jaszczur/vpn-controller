package com.example.vpncontroller.usecases

import com.example.vpncontroller.modules.stats.VpnStatsAdapter
import com.example.vpncontroller.modules.countries.Countries
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/vpn/stats")
class VpnStatisticsUseCase(private val vpnStatsRest: VpnStatsAdapter,
                           private val countries: Countries) {

    @GetMapping("/country/{country}")
    fun serverStats(@PathVariable countryName: String) =
            Mono.justOrEmpty(countries.byCode(countryName))
                    .flatMapMany(vpnStatsRest::serverStats)

    @GetMapping("/country/{country}/sorted")
    fun sortedStats(@PathVariable countryName: String) =
            serverStats(countryName)
                    .sort(compareBy { it.networkLoad })

    @GetMapping("/country/{country}/best")
    fun findBest(@PathVariable countryName: String) =
            serverStats(countryName)
                    .collectSortedList(compareBy { it.networkLoad })
                    .map { it.firstOrNull() }



}