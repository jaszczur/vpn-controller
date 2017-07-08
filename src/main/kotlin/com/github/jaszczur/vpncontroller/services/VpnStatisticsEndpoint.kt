package com.github.jaszczur.vpncontroller.services

import com.github.jaszczur.vpncontroller.usecases.VpnConnectionUseCase
import com.github.jaszczur.vpncontroller.usecases.VpnStatisticsUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.EmitterProcessor
import reactor.core.publisher.FluxProcessor

data class ManualTriggers(val findBetterServerTrigger: FluxProcessor<Any, Any> = EmitterProcessor.create())

@RestController
@RequestMapping("/vpn")
class VpnStatisticsEndpoint(private val vpnStatsUseCase: VpnStatisticsUseCase,
                            private val vpnConnectionUseCase: VpnConnectionUseCase,
                            manualTriggers: ManualTriggers) {

    private val findBetterServerTrigger = manualTriggers.findBetterServerTrigger.sink()

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
    fun switchToBetterServer(): Unit {
        findBetterServerTrigger.next(0)
    }

    // TODO: should be PUT
    @GetMapping("/switch-to/country/{country}")
    fun switchToBestServerInAnotherCountry(@PathVariable country: String): Unit {
        TODO()
    }
}