package com.github.jaszczur.vpncontroller.modules.vpnconnection.impl.adapters

import com.github.jaszczur.vpncontroller.domain.ConnectableServer
import java.util.*

interface ServerUnitConverter {

    fun fromUnitListLine(unitListLine: String): Optional<ConnectableServer> =
            extractUnitInstance(unitListLine)
                    .flatMap(this::fromUnitInstance)

    fun fromUnitInstance(unitInstance: String): Optional<ConnectableServer>

    fun extractUnitInstance(unitListLine: String): Optional<String>

    fun toUnitInstance(connectableServer: ConnectableServer): String

    fun toUnitName(connectableServer: ConnectableServer): String
}