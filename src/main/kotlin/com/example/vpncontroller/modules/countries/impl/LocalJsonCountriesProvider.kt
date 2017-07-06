package com.example.vpncontroller.modules.countries.impl

import com.example.vpncontroller.domain.Country
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.net.URL

@Service
class LocalJsonCountriesProvider(
        val objectMapper: ObjectMapper,
        @Value("countries.resource") val countriesJsonFile: URL) {

    fun load(): List<Country> {
        val countries = objectMapper.readValue(countriesJsonFile, Array<Country>::class.java)
        return countries.toList()
    }



}