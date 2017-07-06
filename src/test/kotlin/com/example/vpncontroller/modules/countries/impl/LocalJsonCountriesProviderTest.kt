package com.example.vpncontroller.modules.countries.impl

import com.example.vpncontroller.domain.Country
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class LocalJsonCountriesProviderTest {
    @Test
    fun load_shouldLoadCountriesListFromFile() {
        val url = javaClass.getResource("/countries.json")
        val cut = LocalJsonCountriesProvider(ObjectMapper(), url)
        val result = cut.load()
        assertThat(result)
                .contains(Country("NL", "Netherlands"))
                .contains(Country("PL", "Poland"))

    }
}