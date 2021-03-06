package com.github.jaszczur.vpncontroller.modules.vpnconnection.impl

import com.github.jaszczur.vpncontroller.domain.ConnectionPerformanceMetric
import com.github.jaszczur.vpncontroller.modules.vpnconnection.Monitoring
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import reactor.util.Loggers
import java.net.URL
import java.nio.ByteBuffer
import java.nio.channels.Channels
import java.nio.channels.ReadableByteChannel
import java.time.Duration

@Service
class ProbeRemoteResourceMonitoring(
        val probeUrl: URL,
        val period: Duration) : Monitoring {

    companion object {
        val logger = Loggers.getLogger(ProbeRemoteResourceMonitoring::class.java)
        val MAX_THROUGHPUT = (1024 * 1024 * 1024).toDouble()
    }

    @Autowired
    constructor(@Value("\${vpncontroller.monitoring.remoteUrl}") probeUrl: URL,
                @Value("\${vpncontroller.monitoring.intervalMinutes}") intervalMinutes: Long):
            this(probeUrl, Duration.ofMinutes(intervalMinutes))

    override fun monitor(): Flux<ConnectionPerformanceMetric> =
            Flux.interval(Duration.ofSeconds(5), period)
                    .publishOn(Schedulers.elastic())
                    .map { downloadGatheringStats() }

    private fun downloadGatheringStats(): ConnectionPerformanceMetric {
        val (connTime, inputStream) = measureTime { probeUrl.openStream() }

        val throughputBps = Channels.newChannel(inputStream).use { channel ->
            val (timeMs, size) = measureTime { downloadSample(channel) }
            logger.debug("Got $size bytes of response in $timeMs ms")
            if (timeMs == 0L)
                MAX_THROUGHPUT
            else
                size / (timeMs / 1000.0)
        }

        return ConnectionPerformanceMetric(throughputBps.toLong(), connTime)
    }

    private fun downloadSample(channel: ReadableByteChannel): Int {
        val buff = ByteBuffer.allocate(1024)
        var size = 0
        while (channel.read(buff) != -1) {
            size += buff.limit()
            buff.clear()
        }
        return size
    }

    private fun <T> measureTime(action: () -> T): Pair<Long, T> {
        val startTime = System.currentTimeMillis()
        val result = action()
        val endTime = System.currentTimeMillis()
        return Pair(endTime - startTime, result)
    }
}