package com.example.vpncontroller.domain

data class Country(val code: String, val name: String)

data class ServerId(val country: Country, val number: Int)

data class VpnServerStats(val serverId: ServerId, val networkLoad: Int)

data class ConnectionPerformanceMetric(val throughputBps: Long, val latencyMs: Long)