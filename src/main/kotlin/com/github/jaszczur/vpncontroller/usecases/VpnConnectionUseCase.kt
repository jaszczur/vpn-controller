package com.github.jaszczur.vpncontroller.usecases

import com.github.jaszczur.vpncontroller.domain.ConnectableServer
import com.github.jaszczur.vpncontroller.domain.Protocol
import com.github.jaszczur.vpncontroller.domain.VpnServerStats
import com.github.jaszczur.vpncontroller.modules.countries.Countries
import com.github.jaszczur.vpncontroller.modules.stats.VpnStatsAdapter
import com.github.jaszczur.vpncontroller.modules.vpnconnection.VpnConnection
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class VpnConnectionUseCase(private val vpnConnection: VpnConnection,
                           private val vpnStatsRest: VpnStatsAdapter,
                           private val statisticsUseCase: VpnStatisticsUseCase,
                           private val configuration: Configuration) {

    fun activeConnection(): Mono<VpnServerStats> =
            vpnConnection.active()
                    .flatMap { server ->
                        val serverId = server.serverId
                        vpnStatsRest.serverStats(serverId.country)
                                .filter { stats -> stats.serverId == serverId }
                                .single()
                    }

    fun switchToBestIn(country: String): Mono<ConnectableServer> =
            statisticsUseCase.findBest(country)
                    .map { ConnectableServer(it.serverId, configuration.defaultProtocol) }
                    .flatMap(vpnConnection::enable)

}