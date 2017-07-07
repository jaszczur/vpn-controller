package com.github.jaszczur.vpncontroller.modules.vpnconnection.impl

import com.github.jaszczur.vpncontroller.domain.Protocol
import com.github.jaszczur.vpncontroller.domain.ServerId
import com.github.jaszczur.vpncontroller.modules.countries.Countries
import reactor.core.publisher.Mono
import java.util.regex.Pattern

class SystemdOpenvpnConversions(val countries: Countries) {

    fun unitInstance(id: ServerId, protocol: Protocol): String {
        val country = id.country.code.toLowerCase()
        val number = id.number
        val proto = protocol.name.toLowerCase()

        return "nord-$country-$number-$proto"
    }

    fun unitInstanceToServerId(unitInstance: String): Mono<ServerId> {
        val matcher = Pattern.compile("nord-(\\w+)-(\\d+)-(\\w+)").matcher(unitInstance)
        if(matcher.matches()) {
            val countryOrNot = countries.byCode(matcher.group(1).toUpperCase())
            val number = Integer.parseInt(matcher.group(2))

            return Mono.justOrEmpty(countryOrNot.map { country ->
                ServerId(country, number)
            })
        } else {
            return Mono.error(IllegalStateException("Error while parsing unit instance: " + unitInstance))
        }
    }

    fun extractUnitInstance(unitListLine: String): Mono<String> {
        val matcher = Pattern.compile("^openvpn-client@(.+)\\.service.*").matcher(unitListLine)

        if (matcher.matches()) {
            return Mono.justOrEmpty(matcher.group(1))
        } else {
            return Mono.error(IllegalStateException("Error while parsing unit line: " + unitListLine))
        }
    }
}
