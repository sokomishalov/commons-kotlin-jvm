/**
 * Copyright 2019-2019 the original author or authors.
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

package ru.sokomishalov.commons.spring.swagger

import com.fasterxml.classmate.TypeResolver
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.reactive.function.server.RouterFunctionDsl
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.sokomishalov.commons.core.consts.EMPTY
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.schema.AlternateTypeRules.newRule
import springfox.documentation.service.Contact
import springfox.documentation.service.SecurityScheme
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import java.net.URI.create

/**
 * @author sokomishalov
 */

const val SWAGGER_UI_PAGE = "swagger-ui.html"

const val REDIRECT_TO_SWAGGER = "redirect:${SWAGGER_UI_PAGE}"

fun RouterFunctionDsl.redirectRootToSwagger() {
    (GET("/") or POST("/")) { permanentRedirect(create("/${SWAGGER_UI_PAGE}")).build() }
}

fun Docket.customizeDocket(
        securityContext: SecurityContext? = null,
        securityScheme: SecurityScheme? = null,
        ignoredParameterTypes: List<Class<out Any>> = listOf(ServerHttpRequest::class.java),
        genericModelSubstitutes: List<Class<out Any>> = listOf(ResponseEntity::class.java),
        title: String = EMPTY,
        description: String = EMPTY,
        contact: Contact = Contact("Sokolov Mikhael", "https://t.me/sokomishalov", "sokomishalov@mail.ru"),
        version: String = "1.0.0"
): Docket = this
        .securityContexts(when {
            securityContext != null -> listOf(securityContext)
            else -> emptyList()
        })
        .securitySchemes(when {
            securityScheme != null -> listOf(securityScheme)
            else -> emptyList()
        })
        .ignoredParameterTypes(*ignoredParameterTypes.toTypedArray())
        .genericModelSubstitutes(*genericModelSubstitutes.toTypedArray())
        .alternateTypeRules(
                newRule(
                        TYPE_RESOLVER.resolve(Flux::class.java, FilePart::class.java),
                        TYPE_RESOLVER.resolve(List::class.java, MultipartFile::class.java)
                ),
                newRule(
                        TYPE_RESOLVER.resolve(Mono::class.java, FilePart::class.java),
                        TYPE_RESOLVER.resolve(MultipartFile::class.java)
                )
        )
        .apiInfo(ApiInfoBuilder()
                .title(title)
                .description(description)
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .contact(contact)
                .version(version)
                .build()
        )


private val TYPE_RESOLVER = TypeResolver()