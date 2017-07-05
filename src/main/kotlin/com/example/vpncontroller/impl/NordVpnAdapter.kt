package com.example.vpncontroller.impl

import com.example.vpncontroller.boundary.ports.RestAdapter
import com.example.vpncontroller.domain.Country
import com.example.vpncontroller.domain.ServerId
import com.example.vpncontroller.domain.VpnServerStats
import reactor.core.publisher.Mono
import java.util.stream.Collectors

class NordVpnAdapter(private val restAdapter: RestAdapter) {
    private val endpoint = "https://nordvpn.com/wp-admin/admin-ajax.php?group={group}&country={country}&action={action}"

    fun serverStats(country: Country): Mono<Set<VpnServerStats>> {
        val params = mapOf(
                "group" to "Standard+VPN+servers",
                "country" to country.name,
                "action" to "getGroupRows")

        return restAdapter
                .getMany<NordServer>(endpoint, params)
                .map(this::convertResponse)
                .collect(Collectors.toSet())
    }

    private fun convertResponse(nord: NordServer): VpnServerStats {
        val number = Integer.parseInt(nord.short.substring(2))
        val country = Country(nord.flag, nord.country)
        val id = ServerId(country, number)
        return VpnServerStats(id, nord.load)
    }
}

data class NordServer(val short: String, val flag: String, val country: String, val load: Int)
