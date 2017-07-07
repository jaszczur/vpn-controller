package com.github.jaszczur.vpncontroller.modules.vpnconnection.impl

import com.github.jaszczur.vpncontroller.domain.Country
import com.github.jaszczur.vpncontroller.domain.ServerId
import com.github.jaszczur.vpncontroller.modules.countries.Countries
import org.assertj.core.api.Assertions.*
import org.junit.Test
import org.mockito.BDDMockito.*
import java.util.*

class SystemdOpenvpnConversionsTest {
    val countries = mock(Countries::class.java)
    val cut = SystemdOpenvpnConversions(countries)

    @Test
    fun extractUnitInstance_shouldWork() {
        val result = cut.extractUnitInstance(
                "openvpn-client@nord-nl-21-tcp.service loaded active running OpenVPN tunnel for nord/nl/21/tcp")

        assertThat(result.block())
                .isEqualTo("nord-nl-21-tcp")
    }

    @Test
    fun extractUnitInstance_shouldReturnError_whenParsingFailed() {
        val unitListLine = "something completly different"

        val result = cut.extractUnitInstance(unitListLine)

        assertThat(catchThrowable { result.block() })
                .hasMessageContaining(unitListLine)
    }

    @Test
    fun unitInstanceToServerId_shouldWork() {
        val country = Country("NL", "Netherlands")
        given(countries.byCode("NL")).willReturn(Optional.of(country))

        val result = cut.unitInstanceToServerId("nord-nl-21-tcp")

        assertThat(result.block())
                .isEqualTo(ServerId(country, 21))
    }

    @Test
    fun unitInstanceToServerId_shouldReturnError_whenParsingFailed() {
        val country = Country("NL", "Netherlands")
        given(countries.byCode("NL")).willReturn(Optional.of(country))
        val unitInstance = "siała baba mak"

        val result = cut.unitInstanceToServerId(unitInstance)

        assertThat(catchThrowable { result.block() })
                .hasMessageContaining(unitInstance)
    }
}