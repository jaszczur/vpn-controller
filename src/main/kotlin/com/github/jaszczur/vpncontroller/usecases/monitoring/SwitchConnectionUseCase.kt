package com.github.jaszczur.vpncontroller.usecases.monitoring

import com.github.jaszczur.vpncontroller.domain.ServerId
import com.github.jaszczur.vpncontroller.modules.stats.VpnStatsAdapter
import com.github.jaszczur.vpncontroller.modules.vpnconnection.Monitoring
import com.github.jaszczur.vpncontroller.modules.vpnconnection.VpnConnection
import org.springframework.stereotype.Service
import reactor.util.Loggers

@Service
class SwitchConnectionUseCase(private val monitoring: Monitoring,
                              private val stats: VpnStatsAdapter,
                              private val conn: VpnConnection) {
    companion object {
        private val logger = Loggers.getLogger(SwitchConnectionUseCase::class.java)
    }

    fun beginMonitoring(config: MonitoringConfig): Unit {
        println("Starting to monitor the connection")
        val advisor = ConnectionAdvisor(config.windowSize, config.treshold)
        monitoring.monitor()
                .doOnNext { logger.debug("Measurement: $it") }
                .map(advisor::giveAnAdvice)
                .doOnNext { logger.debug("Advice: $it") }
                .filter { it == Advice.SWITCH }
                .flatMap { conn.active() }
                .flatMap(this::findSimilarButBetter)
                .doOnNext { logger.debug("Switching to: $it") }
                .flatMap { conn.enable(it, config.protocol) }
                .doOnNext { logger.info("Switched to: $it") }
                .subscribe()
    }

    private fun findSimilarButBetter(serverId: ServerId) =
            stats.serverStats(serverId.country)
                    .collectSortedList(compareBy { it.networkLoad })
                    .map { sortedServers ->
                        val result = sortedServers.firstOrNull()
                        result?.serverId ?: serverId
                    }

}
