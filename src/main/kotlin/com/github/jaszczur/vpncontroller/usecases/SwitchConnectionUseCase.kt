package com.github.jaszczur.vpncontroller.usecases

import com.github.jaszczur.vpncontroller.domain.ConnectionPerformanceMetric
import com.github.jaszczur.vpncontroller.modules.vpnconnection.Monitoring
import org.springframework.stereotype.Service
import java.util.*
import javax.annotation.PostConstruct

@Service
class SwitchConnectionUseCase(private val monitoring: Monitoring) {
    @PostConstruct
    fun beginMonitoring(): Unit {
        println("Starting to monitor the connection")
        val advisor = ConnectionAdvisor(5, 0.8)
        monitoring.monitor()
                .map(advisor::giveAnAdvice)
                .subscribe { println("Got advice: $it") }
    }
}

enum class Advice {
    FINE, SWITCH
}

class ConnectionAdvisor(val windowSize: Int, val treshold: Double) {
    private val latestValues = Window<ConnectionPerformanceMetric>(windowSize)

    fun giveAnAdvice(measurement: ConnectionPerformanceMetric): Advice {
        val previousMeasurements = latestValues.values()
        latestValues.add(measurement)

        if (previousMeasurements.size < windowSize) {
            return Advice.FINE
        } else {
            val bandwidthDiff = diff(measurement, previousMeasurements) { it.throughputBps.toDouble() }
            val latencyDiff = diff(measurement, previousMeasurements) { it.latencyMs.toDouble() }

            return if (bandwidthDiff < treshold || latencyDiff < treshold)
                Advice.SWITCH
            else
                Advice.FINE
        }
    }

    private fun diff(measurement: ConnectionPerformanceMetric,
                     previousMeasurements: List<ConnectionPerformanceMetric>,
                     mapper: (ConnectionPerformanceMetric) -> Double): Double {
        val current = mapper(measurement)
        val average = previousMeasurements
                .map(mapper)
                .average()
        return current / average
    }
}

class Window<T>(val maxSize: Int) {
    private val values = LinkedList<T>()

    fun add(elem: T) {
        synchronized(values) {
            if (values.size == maxSize) {
                values.removeLast()
            }
            values.addFirst(elem)
        }
    }

    fun values() = synchronized(values) { values.toList() }
}