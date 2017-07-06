package com.example.vpncontroller.services

import com.example.vpncontroller.domain.VpnServerStats
import com.example.vpncontroller.usecases.VpnStatisticsUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.server.ServerResponse

@RestController
@RequestMapping("/vpn/stats")
class VpnStatisticsEndpoint(private val vpnStatsUseCase: VpnStatisticsUseCase) {

    @GetMapping("/country/{country}")
    fun serverStats(@PathVariable country: String) =
            vpnStatsUseCase.serverStats(country)

    @GetMapping("/country/{country}/sorted")
    fun sortedStats(@PathVariable country: String) =
            vpnStatsUseCase.sortedStats(country)

    @GetMapping("/country/{country}/best")
    fun findBest(@PathVariable country: String) =
            vpnStatsUseCase.findBest(country)

}