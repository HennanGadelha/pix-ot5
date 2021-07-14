package br.com.zup.handlers

import io.micronaut.aop.Around
import java.lang.annotation.Documented
import kotlin.annotation.AnnotationTarget.*

@Documented
@Retention(AnnotationRetention.RUNTIME)
@Target( CLASS, FIELD, TYPE)
@Around
annotation class ErrorAroundHandler
