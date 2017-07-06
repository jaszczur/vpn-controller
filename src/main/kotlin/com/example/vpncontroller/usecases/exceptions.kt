package com.example.vpncontroller.usecases

data class CountryNotFoundException(val countryDescription: String) :
        Exception("Country not found: $countryDescription")
