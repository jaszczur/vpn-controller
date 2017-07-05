package com.example.vpncontroller.domain

data class Country(val code: String, val name: String) {
    companion object {
        fun byName(name: String) = Country("xx", name)
    }
}

data class ServerId(val country: Country, val number: Int)

data class VpnServerStats(val serverId: ServerId, val networkLoad: Int)
