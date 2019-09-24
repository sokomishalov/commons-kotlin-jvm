@file:Suppress("unused")

package ru.sokomishalov.commons.spring.config

import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.ResourceHandlerRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer
import ru.sokomishalov.commons.spring.serialization.JACKSON_DECODER
import ru.sokomishalov.commons.spring.serialization.JACKSON_ENCODER

open class CustomWebFluxConfigurer : WebFluxConfigurer {

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.apply {
            addResourceHandler("/swagger-ui.html**").addResourceLocations("classpath:/META-INF/resources/")
            addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/")
        }
    }

    override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
        configurer.defaultCodecs().apply {
            jackson2JsonEncoder(JACKSON_ENCODER)
            jackson2JsonDecoder(JACKSON_DECODER)
        }
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry
                .addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .maxAge(3600)
    }
}