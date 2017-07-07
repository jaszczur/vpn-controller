package com.github.jaszczur.vpncontroller.modules.vpnconnection.impl

import com.github.jaszczur.vpncontroller.domain.ConnectionPerformanceMetric
import com.github.jaszczur.vpncontroller.modules.vpnconnection.Monitoring
import org.springframework.beans.factory.annotation.Value
import reactor.core.publisher.Flux
import java.net.URL
import java.nio.ByteBuffer
import java.nio.channels.Channels
import java.time.Duration

class ProbeRemoteResourceMonitoring(@Value("#{monitoring.remoteUrl}") val probeUrl: URL) : Monitoring {


    override fun monitor(): Flux<ConnectionPerformanceMetric> =
        Flux.interval(Duration.ofSeconds(5), Duration.ofMinutes(10))
                .map { downloadGatheringStats() }

    private fun downloadGatheringStats(): ConnectionPerformanceMetric {
        val bufferSize = 1024
        val buff = ByteBuffer.allocate(bufferSize)

        val throughputBps = Channels.newChannel(probeUrl.openStream()).use { channel ->
            var size = 0
            val timeMs = measureTime {
                while(channel.read(buff) != -1) {
                    size += buff.limit()
                    buff.clear()
                }
            }
            println(size)
            size.toDouble() / (timeMs / 1000)
        }

        return ConnectionPerformanceMetric(throughputBps.toLong(), 0)

    }

    private fun measureTime(action: () -> Unit): Long {
        val startTime = System.currentTimeMillis()
        action()
        val endTime = System.currentTimeMillis()
        return endTime - startTime;
    }
}