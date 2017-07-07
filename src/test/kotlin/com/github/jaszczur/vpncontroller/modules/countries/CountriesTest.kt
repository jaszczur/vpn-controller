package com.github.jaszczur.vpncontroller.modules.countries

import com.github.jaszczur.vpncontroller.domain.Country
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

    @Test
    fun byCode_shouldReturnCountry_ifItIsPresent() {
        assertThat(cut.byCode("US"))
                .isEqualTo(Optional.of(Country("US", "United States")))

    }

    @Test
    fun byCode_shouldNotReturnCountry_ifItIsAbsent() {
        assertThat(cut.byCode("SEB"))
                .isEmpty
    }
}