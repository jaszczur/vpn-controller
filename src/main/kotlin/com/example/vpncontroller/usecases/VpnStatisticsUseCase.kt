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
    fun serverStats(@PathVariable country: String) =
            Mono.justOrEmpty(countries.byCode(country))
                    .flatMapMany(vpnStatsRest::serverStats)

    @GetMapping("/country/{country}/sorted")
    fun sortedStats(@PathVariable country: String) =
            serverStats(country)
                    .sort(compareBy { it.networkLoad })

    @GetMapping("/country/{country}/best")
    fun findBest(@PathVariable country: String) =
            serverStats(country)
                    .collectSortedList(compareBy { it.networkLoad })
                    .map { it.firstOrNull() }



}