package com.github.jaszczur.vpncontroller.modules.vpnconnection.impl

import com.github.jaszczur.vpncontroller.domain.ConnectionPerformanceMetric
import com.github.jaszczur.vpncontroller.modules.vpnconnection.Monitoring
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.net.URL
import java.nio.ByteBuffer
import java.nio.channels.Channels
import java.nio.channels.ReadableByteChannel
import java.time.Duration

@Service
class ProbeRemoteResourceMonitoring(
        val probeUrl: URL,
        val period: Duration) : Monitoring {

    @Autowired
    constructor(@Value("\${monitoring.remoteUrl}") probeUrl: URL,
                @Value("\${monitoring.intervalMinutes}") intervalMinutes: Long):
            this(probeUrl, Duration.ofMinutes(intervalMinutes))

    override fun monitor(): Flux<ConnectionPerformanceMetric> =
            Flux.interval(Duration.ofSeconds(5), period)
                    .map { downloadGatheringStats() }

    private fun downloadGatheringStats(): ConnectionPerformanceMetric {
        val (connTime, inputStream) = measureTime { probeUrl.openStream() }

        val throughputBps = Channels.newChannel(inputStream).use { channel ->
            val (timeMs, size) = measureTime { downloadSample(channel) }
            size.toDouble() / (timeMs / 1000)
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