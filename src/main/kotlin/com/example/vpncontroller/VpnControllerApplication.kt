package com.example.vpncontroller

import com.example.vpncontroller.modules.countries.Countries
import com.example.vpncontroller.modules.countries.impl.LocalJsonCountriesProvider
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import java.util.*


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

}

fun main(args: Array<String>) {
    SpringApplication.run(VpnControllerApplication::class.java, *args)
}
