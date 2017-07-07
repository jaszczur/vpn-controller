package com.github.jaszczur.vpncontroller.modules.vpnconnection.impl

import org.assertj.core.api.Assertions
import org.junit.Test

class CommandTest {
    @Test
    fun executeGettingOutput_shouldReturnCommandOutput() {
        val result = Command("echo", "-n", "dupa\ncycki\n")
            .executeGettingOutput()
                .collectList()
                .block()

        Assertions.assertThat(result).containsExactly("dupa", "cycki")
    }
}