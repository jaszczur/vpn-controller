package com.example.vpncontroller.modules.countries

import com.example.vpncontroller.domain.Country
import java.util.*

class Countries(countries: Set<Country>) {
    private val byNames = countries.associateBy { it.name }
    private val byCodes = countries.associateBy { it.code }

    fun byName(name: String): Optional<Country> = Optional.ofNullable(byNames[name])
    fun byCode(code: String): Optional<Country> = Optional.ofNullable(byCodes[code])
}
