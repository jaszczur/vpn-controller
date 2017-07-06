package com.example.vpncontroller.modules.countries

import com.example.vpncontroller.domain.Country
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*

class CountriesTest {
    val cut = Countries(setOf(
            Country("PL", "Poland"),
            Country("US", "United States")))

    @Test
    fun byName_shouldReturnCountry_ifItIsPresent() {
        assertThat(cut.byName("Poland"))
                .isEqualTo(Optional.of(Country("PL", "Poland")))

    }

    @Test
    fun byName_shouldNotReturnCountry_ifItIsAbsent() {
        assertThat(cut.byName("San Escobar"))
                .isEmpty
    }
}