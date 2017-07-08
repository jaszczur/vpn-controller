package com.github.jaszczur.vpncontroller.usecases.monitoring

import com.github.jaszczur.vpncontroller.domain.Protocol

data class MonitoringConfig(val protocol: Protocol,
                            val windowSize: Int = 5,
                            val treshold: Double = 0.7)