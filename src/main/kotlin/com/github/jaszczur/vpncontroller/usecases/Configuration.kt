package com.github.jaszczur.vpncontroller.usecases

import com.github.jaszczur.vpncontroller.domain.Protocol

data class Configuration(val defaultProtocol: Protocol,
                         val monitoringWindowSize: Int = 5,
                         val monitoringThreshold: Double = 0.7)