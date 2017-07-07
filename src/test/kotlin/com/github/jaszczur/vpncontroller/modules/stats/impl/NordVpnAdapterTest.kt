package com.github.jaszczur.vpncontroller.modules.stats.impl

import com.github.jaszczur.vpncontroller.domain.Country
import com.github.jaszczur.vpncontroller.modules.rest.impl.SpringRestAdapter
import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.Test
import java.util.stream.Collectors

internal class NordVpnAdapterTest {
    @Test
    @Ignore("Integration test with real server")
    internal fun integration() {
        val cut = NordVpnAdapter(SpringRestAdapter())
        val result = cut.serverStats(Country("AL", "Albania"))
                .collect(Collectors.toSet())
                .block()
        assertThat(result).isNotEmpty
        assertThat(result)
                .allMatch { it.serverId.country.code == "AL"}
                .allMatch { it.serverId.country.name == "Albania"}
    }
}