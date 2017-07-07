package com.github.jaszczur.vpncontroller.modules.vpnconnection.impl

import org.junit.Ignore
import org.junit.Test
import java.net.URL

class ProbeRemoteResourceMonitoringTest {
    @Test
    @Ignore
    fun test_integration() {
        val cut = ProbeRemoteResourceMonitoring(URL("http://central.maven.org/maven2/io/reactivex/rxjava2/rxjava/2.1.1/rxjava-2.1.1.jar"))

        val values = cut.monitor()
                .take(3)
                .doOnNext(::println)
                .collectList()
                .block()

        println("Done and collected ${values.size} samples")
    }
}