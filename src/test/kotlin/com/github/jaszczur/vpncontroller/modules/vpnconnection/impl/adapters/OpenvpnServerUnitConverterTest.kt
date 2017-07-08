package com.github.jaszczur.vpncontroller.modules.vpnconnection.impl.adapters


import com.github.jaszczur.vpncontroller.domain.ConnectableServer
import com.github.jaszczur.vpncontroller.domain.Country
import com.github.jaszczur.vpncontroller.domain.Protocol
import com.github.jaszczur.vpncontroller.domain.ServerId
import com.github.jaszczur.vpncontroller.modules.countries.Countries
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.mock
import java.util.*

class OpenvpnServerUnitConverterTest {

    val countries = mock(Countries::class.java)
    val cut = OpenvpnServerUnitConverter(countries)

    @Test
    fun fromUnitListLine_todo() {
        TODO()
    }

    @Test
    fun extractUnitInstance_shouldWork() {
        val result = cut.extractUnitInstance(
                "openvpn-client@nord-nl-21-tcp.service loaded active running OpenVPN tunnel for nord/nl/21/tcp")

        assertThat(result)
                .contains("nord-nl-21-tcp")
    }

    @Test
    fun extractUnitInstance_shouldThrowError_whenParsingFailed() {
        val unitListLine = "something completely different"

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
    fun unitInstanceToServerId_shouldThrowError_whenParsingFailed() {
        val country = Country("NL", "Netherlands")
        given(countries.byCode("NL")).willReturn(Optional.of(country))
        val unitInstance = "siała baba mak"

        assertThat(catchThrowable { cut.fromUnitInstance(unitInstance) })
                .hasMessageContaining(unitInstance)
    }

    @Test
    fun toUnitName_shouldConstructProperUnitName() {
        val server = ConnectableServer(
                ServerId(Country("PL", "Poland"), 69),
                Protocol.UDP)
        val result = cut.toUnitName(server)
        assertThat(result).isEqualTo("openvpn-client@nord-pl-69-udp.service")
    }

    @Test
    fun toUnitInstance_shouldConstructProperUnitInstance() {
        val server = ConnectableServer(
                ServerId(Country("PL", "Poland"), 69),
                Protocol.UDP)
        val result = cut.toUnitInstance(server)
        assertThat(result).isEqualTo("nord-pl-69-udp")
    }


}