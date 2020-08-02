package ru.sokomishalov.commons.spring.swagger

import com.fasterxml.classmate.ResolvedType
import com.fasterxml.classmate.TypeResolver
import org.springframework.web.method.HandlerMethod
import springfox.documentation.spring.web.readers.operation.HandlerMethodResolver
import kotlin.reflect.jvm.javaType
import kotlin.reflect.jvm.kotlinFunction

/**
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