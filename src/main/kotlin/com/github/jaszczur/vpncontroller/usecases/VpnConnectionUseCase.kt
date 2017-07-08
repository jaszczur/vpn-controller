package com.github.jaszczur.vpncontroller.usecases

import com.github.jaszczur.vpncontroller.domain.VpnServerStats
import com.github.jaszczur.vpncontroller.modules.stats.VpnStatsAdapter
import com.github.jaszczur.vpncontroller.modules.vpnconnection.VpnConnection
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class VpnConnectionUseCase(private val vpnConnection: VpnConnection,
                           private val vpnStatsRest: VpnStatsAdapter) {

    fun activeConnection(): Mono<VpnServerStats> =
            vpnConnection.active()
                    .flatMap { server ->
                        val serverId = server.serverId
                        vpnStatsRest.serverStats(serverId.country)
                                .filter{ stats -> stats.serverId == serverId }
                                .single()
                    }
}