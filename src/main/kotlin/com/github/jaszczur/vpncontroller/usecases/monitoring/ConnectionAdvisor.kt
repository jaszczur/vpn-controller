package com.github.jaszczur.vpncontroller.usecases.monitoring

import com.github.jaszczur.vpncontroller.domain.ConnectionPerformanceMetric
import com.github.jaszczur.vpncontroller.utilities.Window


internal enum class Advice {
    FINE, SWITCH
}

internal class ConnectionAdvisor(val windowSize: Int, val treshold: Double) {
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