package com.github.jaszczur.vpncontroller.usecases.monitoring

import com.github.jaszczur.vpncontroller.domain.Protocol
import com.github.jaszczur.vpncontroller.domain.ServerId
import com.github.jaszczur.vpncontroller.modules.stats.VpnStatsAdapter
import com.github.jaszczur.vpncontroller.modules.vpnconnection.Monitoring
import com.github.jaszczur.vpncontroller.modules.vpnconnection.VpnConnection
import org.reactivestreams.Publisher
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.util.Loggers

@Service
class SwitchConnectionUseCase(private val monitoring: Monitoring,
                              private val stats: VpnStatsAdapter,
                              private val conn: VpnConnection) {
    companion object {
        private val logger = Loggers.getLogger(SwitchConnectionUseCase::class.java)
    }

    fun beginMonitoring(config: MonitoringConfig, manualTrigger: Flux<Any>): Unit {
        logger.info("Starting to monitor the connection")

        streamOfSwitchAdvices(config, manualTrigger)
                .transform(this::findBetterServer)
                .transform(this.switchVpnServer(config.protocol))
                .subscribe()
    }

    private fun streamOfSwitchAdvices(config: MonitoringConfig, manualTrigger: Flux<Any>): Flux<Advice> {
        val advicesFromTimer = advicesFromTimer(config)
        val advicesFromTrigger = advicesFromTrigger(manualTrigger)
        return Flux.merge(advicesFromTimer, advicesFromTrigger)
    }

    private fun advicesFromTimer(config: MonitoringConfig): Flux<Advice>? {
        val advisor = ConnectionAdvisor(config.windowSize, config.threshold)

        val advicesFromTimer = monitoring.monitor()
                .doOnNext { logger.debug("Measurement: $it") }
                .map(advisor::giveAnAdvice)
                .doOnNext { logger.debug("Advice: $it") }
                .filter { it == Advice.SWITCH }
        return advicesFromTimer
    }

    private fun advicesFromTrigger(manualTrigger: Flux<Any>) = manualTrigger.map { Advice.SWITCH }

    private fun findBetterServer(advices: Flux<Advice>): Publisher<ServerId> {
        return advices.flatMap { conn.active() }
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

    private fun switchVpnServer(protocol: Protocol): (Flux<ServerId>) -> Flux<ServerId> =
            { serverIds: Flux<ServerId> ->
                serverIds.flatMap { conn.enable(it, protocol) }
                        .doOnNext { logger.info("Switched to: $it") }
            }

}
