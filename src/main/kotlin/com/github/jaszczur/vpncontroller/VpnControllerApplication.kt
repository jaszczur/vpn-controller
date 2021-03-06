package com.github.jaszczur.vpncontroller

import com.github.jaszczur.vpncontroller.domain.Protocol
import com.github.jaszczur.vpncontroller.modules.countries.impl.LocalJsonCountriesProvider
import com.github.jaszczur.vpncontroller.usecases.Configuration
import com.github.jaszczur.vpncontroller.usecases.monitoring.MonitorConnectionUseCase
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.PostConstruct

@SpringBootApplication
class VpnControllerApplication {

    @Bean
    fun countries(countriesProvider: LocalJsonCountriesProvider) = countriesProvider.create()

    // Uncomment to inspect
//    @Bean
    fun inspectBeans(ctx: ApplicationContext) = ApplicationRunner { args ->
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
            Configuration(protocol, windowSize, threshold)

}

@Component
class MonitoringStarter(val monitorConnectionUseCase: MonitorConnectionUseCase) {

    @PostConstruct
    fun beginMonitoring(): Unit {
        monitorConnectionUseCase.beginMonitoring()
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(VpnControllerApplication::class.java, *args)
}
