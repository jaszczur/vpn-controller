package com.example.vpncontroller.service

import com.example.vpncontroller.boundary.api.VpnStatsAdapter
import com.example.vpncontroller.domain.Country
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/vpn")
class VpnServiceEndpoint(private val vpnStatsRest: VpnStatsAdapter) {

    @GetMapping("/by-country-name/{countryName}")
    fun serverStats(@PathVariable countryName: String) =
            vpnStatsRest.serverStats(Country.byName(countryName))


    @GetMapping("/by-country-name/{countryName}/sorted")
    fun sortedStats(@PathVariable countryName: String) =
            serverStats(countryName)
                    .sort(compareBy { it.networkLoad })

    @GetMapping("/by-country-name/{countryName}/best")
    fun findBest(@PathVariable countryName: String) =
            serverStats(countryName)
                    .collectSortedList(compareBy { it.networkLoad })
                    .map { it.firstOrNull() }



}