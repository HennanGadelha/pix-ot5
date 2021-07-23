package br.com.zup.handlers

import br.com.zup.handlers.exceptions.ChaveNaoEncontradaException
import br.com.zup.handlers.exceptions.ChavePixExistenteException
import br.com.zup.handlers.exceptions.ClienteNaoExistenteExistenteException
import io.grpc.Status
import io.grpc.stub.StreamObserver
import io.micronaut.aop.InterceptorBean
import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import java.lang.IllegalStateException
import javax.inject.Singleton
import javax.validation.ConstraintViolationException

@Singleton
@InterceptorBean(ErrorAroundHandler::class)
class ErrorAroundHandlerInterceptor : MethodInterceptor<Any, Any> {
    override fun intercept(context: MethodInvocationContext<Any, Any>): Any? {

        try {
            context.proceed()
        }catch (ex: Exception){

            val responseObserver = context.parameterValues[1] as StreamObserver<*>

            val status = when(ex) {

                is ConstraintViolationException -> Status.INVALID_ARGUMENT
                    .withCause(ex)
                    .withDescription(ex.message)

                is ChavePixExistenteException -> Status.ALREADY_EXISTS
                    .withCause(ex)
                    .withDescription("Chave pix já cadastrada")

                is ChaveNaoEncontradaException -> Status.NOT_FOUND
                    .withCause(ex)
                    .withDescription("Chave pix nao encontrado ou nao pertence ao cliente")

                is ClienteNaoExistenteExistenteException -> Status.NOT_FOUND
                    .withCause(ex)
                    .withDescription("Cliente nao encontrado")

                else  ->
                {
                    println(ex)
                    println(ex.message)
                    Status.UNKNOWN
                    .withCause(ex)
                    .withDescription("Erro inesperado")}

            }

            responseObserver.onError(status.asRuntimeException())
        }

        return  null

    }

}