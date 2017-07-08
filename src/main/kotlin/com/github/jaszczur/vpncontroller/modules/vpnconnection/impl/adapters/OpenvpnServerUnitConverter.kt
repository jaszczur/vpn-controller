package com.github.jaszczur.vpncontroller.modules.vpnconnection.impl.adapters

import com.github.jaszczur.vpncontroller.domain.ConnectableServer
import com.github.jaszczur.vpncontroller.domain.Protocol
import com.github.jaszczur.vpncontroller.domain.ServerId
import com.github.jaszczur.vpncontroller.modules.countries.Countries
import java.util.*
import java.util.regex.Pattern

class OpenvpnServerUnitConverter(private val countries: Countries) : ServerUnitConverter {
    companion object {
        private val unitPattern = Pattern.compile(
                "(\\p{Alpha}+)(\\p{Digit}+)\\.nordvpn\\.com\\.(\\p{Alpha}+)")

        private val linePattern = Pattern.compile(
                "^openvpn-client@(.+)\\.service.*")
    }


    override fun fromUnitInstance(unitInstance: String): Optional<ConnectableServer> {
        val matcher = unitPattern.matcher(unitInstance)
        if (matcher.matches()) {
            val countryOrNot = countries.byCode(matcher.group(1).toUpperCase())
            val number = Integer.parseInt(matcher.group(2))
            val proto = Protocol.valueOf(matcher.group(3).toUpperCase())

            return countryOrNot.map { country ->
                ConnectableServer(ServerId(country, number), proto)
            }
        } else {
            throw IllegalStateException("Error while parsing unit instance: " + unitInstance)
        }
    }

    override fun extractUnitInstance(unitListLine: String): Optional<String> {
        val matcher = linePattern.matcher(unitListLine)

        if (matcher.matches()) {
            return Optional.ofNullable(matcher.group(1))
        } else {
            throw IllegalStateException("Error while parsing unit line: " + unitListLine)
        }
    }

    override fun toUnitInstance(connectableServer: ConnectableServer): String {
        val country = connectableServer.serverId.country.code.toLowerCase()
        val number = connectableServer.serverId.number
        val proto = connectableServer.protocol.name.toLowerCase()

        return "$country$number.nordvpn.com.$proto"
    }

    override fun toUnitName(connectableServer: ConnectableServer): String {
        val instance = toUnitInstance(connectableServer)
        return "openvpn-client@$instance.service"
    }
}