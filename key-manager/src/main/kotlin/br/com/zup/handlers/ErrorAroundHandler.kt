package br.com.zup.handlers

import io.micronaut.aop.Around
import io.micronaut.context.annotation.Type
import java.lang.annotation.Documented
import kotlin.annotation.AnnotationTarget.*

@Documented
@Retention(AnnotationRetention.RUNTIME)
@Target( CLASS, FIELD, TYPE)
@Around
@Type(ErrorAroundHandlerInterceptor::class)
annotation class ErrorAroundHandler
