package com.github.jaszczur.vpncontroller.modules.stats.impl

import com.github.jaszczur.vpncontroller.modules.stats.VpnStatsAdapter
import com.github.jaszczur.vpncontroller.modules.rest.RestAdapter
import com.github.jaszczur.vpncontroller.domain.Country
import com.github.jaszczur.vpncontroller.domain.ServerId
import com.github.jaszczur.vpncontroller.domain.VpnServerStats
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
@Qualifier("nordvpn.com")
class NordVpnAdapter(private val restAdapter: RestAdapter) : VpnStatsAdapter {
    private val endpoint = "https://nordvpn.com/wp-admin/admin-ajax.php?group={group}&country={country}&action={action}"

    override fun serverStats(country: Country): Flux<VpnServerStats> {
        val params = mapOf(
                "group" to "Standard+VPN+servers",
                "country" to country.name,
                "action" to "getGroupRows")

        return restAdapter
                .getMany<NordServer>(endpoint, params)
                .map(this::convertResponse)
    }

    private fun convertResponse(nord: NordServer): VpnServerStats {
        val number = Integer.parseInt(nord.short.substring(2))
        val country = Country(nord.flag, nord.country)
        val id = ServerId(country, number)
        return VpnServerStats(id, nord.load)
    }

    private data class NordServer(val short: String,
                                  val flag: String,
                                  val country: String,
                                  val load: Int)
}

