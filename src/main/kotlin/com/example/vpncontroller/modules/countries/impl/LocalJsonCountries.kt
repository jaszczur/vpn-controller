package com.example.vpncontroller.modules.countries.impl

import com.example.vpncontroller.modules.countries.Countries
import com.example.vpncontroller.domain.Country
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.net.URL
import java.util.*
import javax.annotation.PostConstruct

@Service
class LocalJsonCountries: Countries {

    @PostConstruct
    fun load(@Value("countries.resource") countriesJsonFile: URL): List<Country> {
        return emptyList()
    }

    override fun byName(name: String): Optional<Country> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun byCode(code: String): Optional<Country> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}