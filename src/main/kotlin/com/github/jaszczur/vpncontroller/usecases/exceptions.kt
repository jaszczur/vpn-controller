package com.github.jaszczur.vpncontroller.usecases

data class CountryNotFoundException(val countryDescription: String) :
        Exception("Country not found: $countryDescription")
