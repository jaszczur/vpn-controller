package com.example.vpncontroller.modules.vpnconnection.impl

import com.example.vpncontroller.domain.Country
import com.example.vpncontroller.domain.ServerId
import com.example.vpncontroller.modules.countries.Countries
import org.assertj.core.api.Assertions.*
import org.junit.Test
import org.mockito.ArgumentMatchers
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
    fun unitInstanceToServerId_shouldWork() {
        val country = Country("NL", "Netherlands")
        given(countries.byCode("NL")).willReturn(Optional.of(country))

        val result = cut.unitInstanceToServerId("nord-nl-21-tcp")

        assertThat(result.block())
                .isEqualTo(ServerId(country, 21))
    }
}