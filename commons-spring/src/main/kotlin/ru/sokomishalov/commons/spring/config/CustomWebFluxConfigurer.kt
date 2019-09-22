@file:Suppress("unused")

package ru.sokomishalov.commons.spring.config

import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.ResourceHandlerRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer
import ru.sokomishalov.commons.core.serialization.OBJECT_MAPPER

open class CustomWebFluxConfigurer : WebFluxConfigurer {

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.apply {
            addResourceHandler("/swagger-ui.html**")
                    .addResourceLocations("classpath:/META-INF/resources/")
            addResourceHandler("/webjars/**")
                    .addResourceLocations("classpath:/META-INF/resources/webjars/")
        }
    }

    override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
        configurer.defaultCodecs().apply {
            jackson2JsonEncoder(Jackson2JsonEncoder(OBJECT_MAPPER))
            jackson2JsonDecoder(Jackson2JsonDecoder(OBJECT_MAPPER))
        }
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry
                .addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .maxAge(3600);
    }
}