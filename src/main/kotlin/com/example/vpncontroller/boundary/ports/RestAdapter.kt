package com.example.vpncontroller.boundary.ports

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

abstract class RestAdapter {

    inline fun <reified T> getMany(url: String, uriVariables: Map<String, Any?>): Flux<T> =
            getMany(url, uriVariables, T::class.java)

    abstract fun <T> getMany(url: String, uriVariables: Map<String, Any?>, clazz: Class<T>): Flux<T>
}