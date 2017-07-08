package com.github.jaszczur.vpncontroller

import com.github.jaszczur.vpncontroller.domain.Protocol
import com.github.jaszczur.vpncontroller.modules.countries.impl.LocalJsonCountriesProvider
import com.github.jaszczur.vpncontroller.services.ManualTriggers
import com.github.jaszczur.vpncontroller.usecases.monitoring.MonitoringConfig
import com.github.jaszczur.vpncontroller.usecases.monitoring.SwitchConnectionUseCase
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import java.util.*
import javax.annotation.PostConstruct


@SpringBootApplication
class VpnControllerApplication {

    @Bean
    fun countries(countriesProvider: LocalJsonCountriesProvider) = countriesProvider.create()

    @Bean
    fun commandLineRunner(ctx: ApplicationContext) = ApplicationRunner { args ->
        println("Let's inspect the beans provided by Spring Boot:")

        val beanNames = ctx.beanDefinitionNames
        Arrays.sort(beanNames)
        for (beanName in beanNames) {
            println(beanName)
        }
    }

    @Bean
    fun monitoringConfig(@Value("\${vpncontroller.connection.proto}") protocol: Protocol,
                         @Value("\${vpncontroller.monitoring.windowSize}") windowSize: Int,
                         @Value("\${vpncontroller.monitoring.threshold}") threshold: Double) =
            MonitoringConfig(protocol, windowSize, threshold)

    @Bean
    fun manualTriggers() = ManualTriggers()

}

@Service
class MonitoringStarter(val switchConnectionUseCase: SwitchConnectionUseCase,
                        val monitoringConfig: MonitoringConfig,
                        val manualTriggers: ManualTriggers) {

    @PostConstruct
    fun beginMonitoring(): Unit {
        switchConnectionUseCase.beginMonitoring(monitoringConfig, manualTriggers.findBetterServerTrigger)
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(VpnControllerApplication::class.java, *args)
}
