package com.example.vpncontroller.usecases

import com.example.vpncontroller.domain.VpnServerStats
import com.example.vpncontroller.modules.stats.VpnStatsAdapter
import com.example.vpncontroller.modules.vpnconnection.VpnConnection
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class VpnConnectionUseCase(private val vpnConnection: VpnConnection,
                           private val vpnStatsRest: VpnStatsAdapter) {

    fun activeConnection(): Mono<VpnServerStats> =
            vpnConnection.active()
                    .flatMap { serverId ->
                        vpnStatsRest.serverStats(serverId.country)
                                .filter{ stats -> stats.serverId == serverId }
                                .single()
                    }
}