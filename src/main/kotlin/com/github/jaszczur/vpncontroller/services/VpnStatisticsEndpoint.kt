package com.github.jaszczur.vpncontroller.services

import com.github.jaszczur.vpncontroller.domain.VpnServerStats
import com.github.jaszczur.vpncontroller.usecases.VpnConnectionUseCase
import com.github.jaszczur.vpncontroller.usecases.VpnStatisticsUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.server.ServerResponse

@RestController
@RequestMapping("/vpn")
class VpnStatisticsEndpoint(private val vpnStatsUseCase: VpnStatisticsUseCase,
                            private val vpnConnectionUseCase: VpnConnectionUseCase) {

    @GetMapping("/country/{country}")
    fun serverStats(@PathVariable country: String) =
            vpnStatsUseCase.serverStats(country)

    @GetMapping("/country/{country}/sorted")
    fun sortedStats(@PathVariable country: String) =
            vpnStatsUseCase.sortedStats(country)

    @GetMapping("/country/{country}/best")
    fun findBest(@PathVariable country: String) =
            vpnStatsUseCase.findBest(country)

    @GetMapping("/active")
    fun activeConnectionStats() =
            vpnConnectionUseCase.activeConnection()
}