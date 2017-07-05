package com.example.vpncontroller.main

import com.example.vpncontroller.boundary.ports.RestAdapter
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux

@Service
class SpringRestAdapter: RestAdapter() {

    override fun <T> getMany(url: String, uriVariables: Map<String, Any?>, clazz: Class<T>): Flux<T> =
        WebClient.create()
                .get()
                .uri(url, uriVariables)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMapMany { response -> response.bodyToFlux(clazz) }

}