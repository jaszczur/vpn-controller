package com.github.jaszczur.vpncontroller.domain

data class Country(val code: String, val name: String)

data class ServerId(val country: Country, val number: Int) {
    override fun toString() = "${country.name} #$number"
}

data class ConnectableServer(val serverId: ServerId, val protocol: Protocol)

data class VpnServerStats(val serverId: ServerId, val networkLoad: Int)

enum class Protocol {
    TCP,
    UDP
}

data class ConnectionPerformanceMetric(val throughputBps: Long, val latencyMs: Long)
