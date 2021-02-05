/**
 * Copyright (c) 2019-present Mikhael Sokolov
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
import org.springframework.boot.info.BuildProperties
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.multipart.MultipartFile
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.RequestHandlerSelectors.basePackage
import springfox.documentation.schema.AlternateTypeRules.newRule
import springfox.documentation.service.Contact
import springfox.documentation.service.SecurityScheme
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.DocumentationType.OAS_30
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future

/**
 * @author sokomishalov
 */

const val SWAGGER_UI_PAGE = "swagger-ui/"
const val REDIRECT_TO_SWAGGER = "redirect:${SWAGGER_UI_PAGE}"
private val DEFAULT_AUTHOR = Contact("Sokolov Mikhael", "https://sokomishalov.github.io/about-me", "sokomishalov@mail.ru")
private val TYPE_RESOLVER = TypeResolver()

inline fun <reified T : Any> initDocket(type: DocumentationType = OAS_30): Docket {
    return Docket(type)
        .select()
        .apis(basePackage(T::class.java.`package`.name))
        .paths { true }
        .build()
}

fun Docket.customize(
    securityContext: SecurityContext? = null,
    securityScheme: SecurityScheme? = null,
    ignoredParameterTypes: List<Class<out Any>> = emptyList(),
    genericModelSubstitutes: List<Class<out Any>> = listOf(ResponseEntity::class.java, CompletableFuture::class.java, Future::class.java),
    useDefaultResponseMessages: Boolean = false,
    buildProperties: BuildProperties? = null,
    title: String = buildProperties?.name.orEmpty(),
    description: String = buildProperties?.artifact.orEmpty(),
    version: String = buildProperties?.version.orEmpty(),
    contact: Contact = DEFAULT_AUTHOR
): Docket {
    return this
        .useDefaultResponseMessages(useDefaultResponseMessages)
        .securityContexts(
            when {
                securityContext != null -> listOf(securityContext)
                else -> emptyList()
            }
        )
        .securitySchemes(
            when {
                securityScheme != null -> listOf(securityScheme)
                else -> emptyList()
            }
        )
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
        .apiInfo(
            ApiInfoBuilder()
                .title(title)
                .description(description)
                .contact(contact)
                .version(version)
                .build()
        )
}