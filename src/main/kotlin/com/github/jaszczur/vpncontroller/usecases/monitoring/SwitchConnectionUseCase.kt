package com.github.jaszczur.vpncontroller.usecases.monitoring

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
class SwitchConnectionUseCase(private val monitoring: Monitoring,
                              private val countries: Countries,
                              private val stats: VpnStatsAdapter,
                              private val conn: VpnConnection,
                              private val configuration: Configuration) {

    val vpnStatistics = VpnStatistics(stats)

    companion object {
        private val logger = Loggers.getLogger(SwitchConnectionUseCase::class.java)
        val defaultServer = ServerId(Country("NL", "Netherlands"), 21)
    }

    fun beginMonitoring(): Unit {
        logger.info("Starting to monitor the connection")

        advicesFromTimer()
                .transform(this::findBetterServer)
                .transform(this::switchVpnServer)
                .subscribe()
    }

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

    private fun advicesFromTimer(): Flux<Advice> {
        val advisor = ConnectionAdvisor(configuration.monitoringWindowSize, configuration.monitoringThreshold)

        val advicesFromTimer = monitoring.monitor()
                .doOnNext { logger.debug("Measurement: $it") }
                .map(advisor::giveAnAdvice)
                .doOnNext { logger.debug("Advice: $it") }
                .filter { it == Advice.SWITCH }
        return advicesFromTimer
    }

    private fun findBetterServer(advices: Flux<Advice>): Publisher<ServerId> {
        return advices
                .flatMap {
                    conn.active()
                            .map { it.serverId }
                            .defaultIfEmpty(defaultServer)
                }
                .flatMap(this::findSimilarButBetter)
                .doOnNext { logger.debug("Found better server: $it") }
    }

    private fun findSimilarButBetter(serverId: ServerId) =
            stats.serverStats(serverId.country)
                    .collectSortedList(compareBy { it.networkLoad })
                    .map { sortedServers ->
                        val result = sortedServers.firstOrNull()
                        result?.serverId ?: serverId
                    }

    private fun switchVpnServer(serverIds: Flux<ServerId>) =
            serverIds.flatMap { conn.enable(ConnectableServer(it, configuration.defaultProtocol)) }
                    .doOnNext { logger.info("Switched to: $it") }


}
