package com.example.vpncontroller.modules.countries.impl

import com.example.vpncontroller.domain.Country
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class LocalJsonCountriesTest {
    @Test
    fun load_shouldLoadCountriesListFromFile() {
        val cut = LocalJsonCountries()
        val url = javaClass.getResource("/countries.json")
        val result = cut.load(url)
        assertThat(result)
                .contains(Country("NL", "Netherlands"))
                .contains(Country("PL", "Poland"))

    }
}