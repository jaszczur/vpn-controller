package com.example.vpncontroller.impl

import com.example.vpncontroller.domain.Country
import com.example.vpncontroller.main.SpringRestAdapter
import org.junit.Test
import java.util.stream.Collectors


internal class NordVpnAdapterTest {
    @Test
    internal fun integration() {
        val cut = NordVpnAdapter(SpringRestAdapter())
        val result = cut.serverStats(Country("AL", "Albania"))
                .collect(Collectors.toSet())
                .block()
        println(result)
    }
}