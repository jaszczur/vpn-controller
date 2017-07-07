package com.example.vpncontroller.modules.vpnconnection.impl

import com.example.vpncontroller.modules.countries.Countries
import org.assertj.core.api.Assertions.*
import org.junit.Test
import org.mockito.BDDMockito.*

class SystemdOpenvpnConversionsTest {

    private fun createCut(): SystemdOpenvpnConversions {
        val countries = mock(Countries::class.java)
        return SystemdOpenvpnConversions(countries)
    }

    @Test
    fun extractUnitInstance_shouldWork() {
        val cut = createCut()

        val result = cut.extractUnitInstance(
                "openvpn-client@nord-nl-21-tcp.service loaded active running OpenVPN tunnel for nord/nl/21/tcp")

        assertThat(result.block())
                .isEqualTo("nord-nl-21-tcp")
    }
}