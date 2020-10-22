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
package ru.sokomishalov.commons.spring.swagger

import com.fasterxml.classmate.ResolvedType
import com.fasterxml.classmate.TypeResolver
import org.springframework.web.method.HandlerMethod
import springfox.documentation.spring.web.readers.operation.HandlerMethodResolver
import kotlin.reflect.jvm.javaType
import kotlin.reflect.jvm.kotlinFunction

/**
 * @see <a href="https://github.com/springfox/springfox/pull/3480">keep it until this PR is merged</a>
 *
 * @author sokomishalov
 */
class KotlinFriendlyHandlerMethodResolver(
        private val typeResolver: TypeResolver
) : HandlerMethodResolver(typeResolver) {

    override fun methodReturnType(handlerMethod: HandlerMethod?): ResolvedType {
        val kFunction = handlerMethod?.method?.kotlinFunction
        return when {
            kFunction != null -> typeResolver.resolve(kFunction.returnType.javaType)
            else -> super.methodReturnType(handlerMethod)
        }
    }
}