package com.github.jaszczur.vpncontroller.modules.countries.impl

import com.github.jaszczur.vpncontroller.domain.Country
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class LocalJsonCountriesProviderTest {
    @Test
    fun load_shouldLoadCountriesListFromFile() {
        val url = javaClass.getResource("/countries.json")
        val cut = LocalJsonCountriesProvider(jacksonObjectMapper(), url)
        val result = cut.load()
        assertThat(result)
                .contains(Country("NL", "Netherlands"))
                .contains(Country("PL", "Poland"))

    }
}