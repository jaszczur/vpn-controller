package com.github.jaszczur.vpncontroller.usecases

import com.github.jaszczur.vpncontroller.domain.ConnectableServer
import com.github.jaszczur.vpncontroller.domain.Country
import com.github.jaszczur.vpncontroller.domain.Protocol
import com.github.jaszczur.vpncontroller.domain.ServerId
import com.github.jaszczur.vpncontroller.modules.countries.Countries
import com.github.jaszczur.vpncontroller.modules.stats.VpnStatsAdapter
import com.github.jaszczur.vpncontroller.modules.vpnconnection.Monitoring
import com.github.jaszczur.vpncontroller.modules.vpnconnection.VpnConnection
import com.github.jaszczur.vpncontroller.usecases.Configuration
import com.github.jaszczur.vpncontroller.usecases.VpnStatistics
import com.github.jaszczur.vpncontroller.usecases.VpnStatisticsUseCase
import org.reactivestreams.Publisher
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.Loggers

@Service
class SwitchConnectionUseCase(private val countries: Countries,
                              stats: VpnStatsAdapter,
                              private val conn: VpnConnection,
                              private val configuration: Configuration) {

    val vpnStatistics = VpnStatistics(stats)

    fun switchToBetter(): Mono<ConnectableServer> =
            conn.active().map { it.serverId.country }
                    .transform(this::switchToBestInCountry)

    fun switchToBestIn(country: String): Mono<ConnectableServer> =
            countries.fuzzyByCode(country)
                    .transform(this::switchToBestInCountry)

    private fun switchToBestInCountry(country: Mono<Country>): Mono<ConnectableServer> =
            country.flatMap(vpnStatistics::findBest)
                    .map { ConnectableServer(it.serverId, configuration.defaultProtocol) }
                    .flatMap(conn::enable)

}
