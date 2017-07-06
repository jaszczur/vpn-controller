package com.example.vpncontroller.boundary.ports

import com.example.vpncontroller.domain.Country
import java.util.*

interface Countries {
    fun byName(name: String): Optional<Country>
    fun byCode(code: String): Optional<Country>
}
