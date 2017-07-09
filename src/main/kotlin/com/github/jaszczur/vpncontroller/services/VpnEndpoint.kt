package com.github.jaszczur.vpncontroller.services

import com.github.jaszczur.vpncontroller.usecases.SwitchConnectionUseCase
import com.github.jaszczur.vpncontroller.usecases.VpnConnectionUseCase
import com.github.jaszczur.vpncontroller.usecases.VpnStatisticsUseCase
import com.github.jaszczur.vpncontroller.usecases.monitoring.MonitorConnectionUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/vpn")
class VpnEndpoint(private val statisticsUseCase: VpnStatisticsUseCase,
                  private val connectionUseCase: VpnConnectionUseCase,
                  private val monitorConnectionUseCase: MonitorConnectionUseCase,
                  private val switchConnectionUseCase: SwitchConnectionUseCase) {

    @GetMapping("/country/{country}")
    fun serverStats(@PathVariable country: String) =
            statisticsUseCase.serverStats(country)

    @GetMapping("/country/{country}/sorted")
    fun sortedStats(@PathVariable country: String) =
            statisticsUseCase.sortedStats(country)

    @GetMapping("/country/{country}/best")
    fun findBest(@PathVariable country: String) =
            statisticsUseCase.findBest(country)

    @GetMapping("/active")
    fun activeConnectionStats() =
            connectionUseCase.activeConnection()

    // TODO: should be PUT
    @GetMapping("/switch-to/better")
    fun switchToBetterServer() =
            switchConnectionUseCase.switchToBetter()

    // TODO: should be PUT
    @GetMapping("/switch-to/country/{country}")
    fun switchToBestServerInAnotherCountry(@PathVariable country: String) =
            switchConnectionUseCase.switchToBestIn(country)

}