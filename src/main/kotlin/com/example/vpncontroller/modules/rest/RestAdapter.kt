package com.example.vpncontroller.modules.rest

import reactor.core.publisher.Flux

abstract class RestAdapter {

    inline fun <reified T> getMany(url: String, uriVariables: Map<String, Any?>): Flux<T> =
            getMany(url, uriVariables, T::class.java)

    abstract fun <T> getMany(url: String, uriVariables: Map<String, Any?>, clazz: Class<T>): Flux<T>
}