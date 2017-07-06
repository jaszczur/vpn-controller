package com.example.vpncontroller.main

import com.example.vpncontroller.boundary.ports.Countries
import com.example.vpncontroller.domain.Country
import org.springframework.stereotype.Service
import java.util.*
import javax.annotation.PostConstruct

@Service
class LocalJsonCountries: Countries {


    @PostConstruct
    fun load() {

    }

    override fun byName(name: String): Optional<Country> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun byCode(code: String): Optional<Country> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}