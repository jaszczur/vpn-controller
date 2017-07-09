package com.github.jaszczur.vpncontroller.usecases.monitoring

import com.github.jaszczur.vpncontroller.domain.ConnectableServer
import com.github.jaszczur.vpncontroller.domain.Country
import com.github.jaszczur.vpncontroller.domain.ServerId
import com.github.jaszczur.vpncontroller.modules.stats.VpnStatsAdapter
import com.github.jaszczur.vpncontroller.modules.vpnconnection.Monitoring
import com.github.jaszczur.vpncontroller.modules.vpnconnection.VpnConnection
import com.github.jaszczur.vpncontroller.usecases.Configuration
import org.reactivestreams.Publisher
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.util.Loggers

@Service
class MonitorConnectionUseCase(private val monitoring: Monitoring,
                               private val stats: VpnStatsAdapter,
                               private val conn: VpnConnection,
                               private val configuration: Configuration) {

    companion object {
        private val logger = Loggers.getLogger(MonitorConnectionUseCase::class.java)
        val defaultServer = ServerId(Country("NL", "Netherlands"), 21)
    }

    fun beginMonitoring(): Unit {
        logger.info("Starting to monitor the connection")

        advicesFromTimer()
                .transform(this::findBetterServer)
                .transform(this::switchVpnServer)
                .subscribe()
    }

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
