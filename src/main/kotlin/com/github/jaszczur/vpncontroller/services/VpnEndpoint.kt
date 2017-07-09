package com.github.jaszczur.vpncontroller.services

import com.github.jaszczur.vpncontroller.usecases.VpnConnectionUseCase
import com.github.jaszczur.vpncontroller.usecases.VpnStatisticsUseCase
import com.github.jaszczur.vpncontroller.usecases.monitoring.SwitchConnectionUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.EmitterProcessor
import reactor.core.publisher.FluxProcessor

@RestController
@RequestMapping("/vpn")
class VpnEndpoint(private val vpnStatsUseCase: VpnStatisticsUseCase,
                  private val vpnConnectionUseCase: VpnConnectionUseCase,
                  private val vpnSwitchConnectionUseCase: SwitchConnectionUseCase) {

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

    // TODO: should be PUT
    @GetMapping("/switch-to/better")
    fun switchToBetterServer() =
            vpnSwitchConnectionUseCase.switchToBetter()


    // TODO: should be PUT
    @GetMapping("/switch-to/country/{country}")
    fun switchToBestServerInAnotherCountry(@PathVariable country: String) =
            vpnConnectionUseCase.switchToBestIn(country)

}