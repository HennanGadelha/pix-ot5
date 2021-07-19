package br.com.zup.pix.remove

import br.com.zup.ChavePixServiceGrpc
import br.com.zup.RemoveChavePixRequest
import br.com.zup.RemoveChavePixServiceGrpc
import br.com.zup.TipoDaConta
import br.com.zup.conta.ContaAssociada
import br.com.zup.pix.ChavePix
import br.com.zup.pix.ChavePixRepository
import br.com.zup.pix.NovaChavePixServiceTest
import br.com.zup.pix.TipoDeChave
import br.com.zup.servicoexterno.ContaClient
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest(transactional = false)
internal class RemoveChavePixServiceTest(val chavePixRepository: ChavePixRepository,
                                         val grpcClient: RemoveChavePixServiceGrpc.RemoveChavePixServiceBlockingStub){


    lateinit var CHAVE_CADASTRADA: ChavePix


    @BeforeEach
    fun setup(){

        CHAVE_CADASTRADA = chavePixRepository.save(
            ChavePix(
                clientId = UUID.randomUUID(),
                tipo = TipoDeChave.CPF,
                chave = "87006987091",
                tipoDaconta = TipoDaConta.CONTA_CORRENTE,
                conta = ContaAssociada(
                    instituicao = "UNIBANCO ITAU SA",
                    nomeDoTitular = "Hennan",
                    cpfDotitular = "87006987091",
                    agencia = "1234",
                    numeroDaConta = "123456"
                )
            )
        )

    }

    @AfterEach
    fun limparBanco(){
        chavePixRepository.deleteAll()
    }


    @Factory
    class Clients {

        @Singleton
        fun gerarClienteGrpc(@GrpcChannel(GrpcServerChannel.NAME) grpcChannel: ManagedChannel)
                : RemoveChavePixServiceGrpc.RemoveChavePixServiceBlockingStub {

            return RemoveChavePixServiceGrpc.newBlockingStub(grpcChannel)
        }
    }


    @Test
    fun `deve apagar chave cadastrada`(){


        val response = grpcClient.remove(RemoveChavePixRequest.newBuilder()
            .setPixId(CHAVE_CADASTRADA.id.toString())
            .setClientId(CHAVE_CADASTRADA.clientId.toString())
            .build())


        with(response){
            assertEquals(CHAVE_CADASTRADA.id.toString(), response.pixId)
            assertEquals(CHAVE_CADASTRADA.clientId.toString(), response.clientId)
        }


    }

    @Test
    fun `nao deve apagar chave inexistente`(){

        val pixIdInexistente = UUID.randomUUID()

        val chaveNaoEncontrada = assertThrows<StatusRuntimeException> {

            grpcClient.remove(
                RemoveChavePixRequest.newBuilder()
                    .setPixId(pixIdInexistente.toString())
                    .setClientId(CHAVE_CADASTRADA.clientId.toString())
                    .build()
            )
        }

        with(chaveNaoEncontrada){
            assertEquals(Status.NOT_FOUND.code, status.code)
        }

    }


    @Test
    fun `nao deve apagar chave na qual nao pertence ao cliemte`(){

        val clientIdInexistente = UUID.randomUUID()

        val chaveNaoEncontrada = assertThrows<StatusRuntimeException> {

            grpcClient.remove(
                RemoveChavePixRequest.newBuilder()
                    .setPixId(clientIdInexistente.toString())
                    .setClientId(CHAVE_CADASTRADA.clientId.toString())
                    .build()
            )
        }

        with(chaveNaoEncontrada){
            assertEquals(Status.NOT_FOUND.code, status.code)
        }

    }

}