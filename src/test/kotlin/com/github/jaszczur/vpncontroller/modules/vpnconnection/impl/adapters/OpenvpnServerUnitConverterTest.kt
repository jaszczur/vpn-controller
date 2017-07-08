package com.github.jaszczur.vpncontroller.modules.vpnconnection.impl.adapters


import com.github.jaszczur.vpncontroller.domain.ConnectableServer
import com.github.jaszczur.vpncontroller.domain.Country
import com.github.jaszczur.vpncontroller.domain.Protocol
import com.github.jaszczur.vpncontroller.domain.ServerId
import com.github.jaszczur.vpncontroller.modules.countries.Countries
import org.assertj.core.api.Assertions.*
import org.junit.Test
import org.mockito.BDDMockito.*
import java.util.*

class OpenvpnServerUnitConverterTest {

    val countries = mock(Countries::class.java)
    val cut = OpenvpnServerUnitConverter(countries)

    @Test
    fun extractUnitInstance_shouldWork() {
        val result = cut.extractUnitInstance(
                "openvpn-client@nord-nl-21-tcp.service loaded active running OpenVPN tunnel for nord/nl/21/tcp")

        assertThat(result)
                .contains("nord-nl-21-tcp")
    }

    @Test
    fun extractUnitInstance_shouldReturnError_whenParsingFailed() {
        val unitListLine = "something completly different"

        assertThat(catchThrowable { cut.extractUnitInstance(unitListLine) })
                .hasMessageContaining(unitListLine)
    }

    @Test
    fun unitInstanceToServerId_shouldWork() {
        val country = Country("NL", "Netherlands")
        given(countries.byCode("NL")).willReturn(Optional.of(country))

        val result = cut.fromUnitInstance("nord-nl-21-tcp")

        assertThat(result)
                .contains(ConnectableServer(ServerId(country, 21), Protocol.TCP))
    }

    @Test
    fun unitInstanceToServerId_shouldReturnError_whenParsingFailed() {
        val country = Country("NL", "Netherlands")
        given(countries.byCode("NL")).willReturn(Optional.of(country))
        val unitInstance = "siała baba mak"

        assertThat(catchThrowable { cut.fromUnitInstance(unitInstance) })
                .hasMessageContaining(unitInstance)
    }
}