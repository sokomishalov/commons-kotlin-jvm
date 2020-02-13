/**
 * Copyright 2019-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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