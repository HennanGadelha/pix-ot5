package br.com.zup.pix

import br.com.zup.CadastraChavePixRequest
import br.com.zup.ChavePixServiceGrpc
import br.com.zup.TipoChavePix
import br.com.zup.TipoDaConta
import br.com.zup.conta.ContaAssociada
import br.com.zup.conta.ContaResponse
import br.com.zup.instituicao.InstituicaoResponse
import br.com.zup.servicoexterno.ContaClient
import br.com.zup.servicoexterno.bacen.*
import br.com.zup.titular.TitularResponse
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.http.HttpResponse
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest(transactional = false)
internal class NovaChavePixServiceTest(
    val chavePixRepository: ChavePixRepository,
    val grpcClient: ChavePixServiceGrpc.ChavePixServiceBlockingStub
) {

    @Inject
    lateinit var itauClient: ContaClient

    @Inject
    lateinit var bacenClient: BacenClient

    companion object {
        val CLIENT_ID = UUID.randomUUID()
    }

    @BeforeEach
    fun setup() {
        chavePixRepository.deleteAll()
    }


    @Test
    fun `deve registrar nova chave pix`() {


        Mockito.`when`(itauClient.consultarConta(clienteId = CLIENT_ID.toString(), tipo = "CONTA_CORRENTE"))
            .thenReturn(HttpResponse.ok(dadosDaContaResponse()))


        Mockito.`when`(bacenClient.criarChavePix(createPixKeyRequest()))
            .thenReturn(HttpResponse.created(createPixKeyResponse()))

        val response = grpcClient.cadastrar(
            CadastraChavePixRequest.newBuilder()
                .setClientId(CLIENT_ID.toString())
                .setTipoChavePix(TipoChavePix.EMAIL)
                .setChave("hennanteste@gmail.com")
                .setTipoDaConta(TipoDaConta.CONTA_CORRENTE)
                .build()
        )



        with(response) {
            assertEquals(CLIENT_ID.toString(), clientId)
            assertNotNull(pixId)
        }

    }

    @Test
    fun `nao deve cadastrar chave pix ja existente`() {

        chavePixRepository.save(
            ChavePix(
                clientId = CLIENT_ID,
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

        val chavePixExistente = assertThrows<StatusRuntimeException> {

            grpcClient.cadastrar(CadastraChavePixRequest
                .newBuilder()
                .setClientId(CLIENT_ID.toString())
                .setTipoChavePix(TipoChavePix.CPF)
                .setChave("87006987091")
                .setTipoDaConta(TipoDaConta.CONTA_CORRENTE)
                .build())

        }

        with(chavePixExistente){
            assertEquals(Status.ALREADY_EXISTS.code, status.code)
            //Chave: '87006987091' já cadastrada

        }

    }

    @Test
    fun `nao deve cadastrar chave pix quando o cliente nao for encontrado`(){

        Mockito.`when`(itauClient.consultarConta(clienteId = CLIENT_ID.toString(), tipo = "CONTA_CORRENTE"))
            .thenReturn(HttpResponse.notFound())

        val clienteNaoEncontrado = assertThrows<StatusRuntimeException> {

            grpcClient.cadastrar(CadastraChavePixRequest
                .newBuilder()
                .setClientId(CLIENT_ID.toString())
                .setTipoChavePix(TipoChavePix.CPF)
                .setChave("87006987091")
                .setTipoDaConta(TipoDaConta.CONTA_CORRENTE)
                .build())

        }

        with(clienteNaoEncontrado){
            assertEquals(Status.NOT_FOUND.code, status.code)
        }

    }

    @Test
    fun `nao deve cadastrar chave pix quando ha dados invalidos`(){

        val requestComDadosInvalidos = assertThrows<StatusRuntimeException> {
            grpcClient.cadastrar(CadastraChavePixRequest.newBuilder().build())
        }

        with(requestComDadosInvalidos){
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
        }

    }

    @Test
    fun `nao deve cadastrar chave pix caso tambem nao cadastre no bacen`(){

        Mockito.`when`(itauClient.consultarConta(clienteId = CLIENT_ID.toString(), tipo = "CONTA_CORRENTE"))
            .thenReturn(HttpResponse.ok(dadosDaContaResponse()))

        Mockito.`when`(bacenClient.criarChavePix(createPixKeyRequest()))
            .thenReturn(HttpResponse.badRequest())

        val response = assertThrows<StatusRuntimeException> { grpcClient.cadastrar(
            CadastraChavePixRequest.newBuilder()
                .setClientId(CLIENT_ID.toString())
                .setTipoChavePix(TipoChavePix.EMAIL)
                .setChave("hennanteste@gmail.com")
                .setTipoDaConta(TipoDaConta.CONTA_CORRENTE)
                .build()
        )}

        with(response) {
            assertEquals(Status.UNKNOWN.code, status.code)
        }

    }


    private fun createPixKeyRequest(): CreatePixKeyRequest {
        return CreatePixKeyRequest(
            keyType = PixKeyType.EMAIL,
            key = "hennanteste@gmail.com",
            bankAccount = bankAccount(),
            owner = owner()
        )
    }

    private fun bankAccount(): BankAccount {
        return BankAccount(
            participant = ContaAssociada.ITAU_UNIBANCO_ISPB,
            branch = "04055",
            accountNumber = "1234",
            accountType = BankAccount.AccountType.CACC
        )
    }

    private fun owner(): Owner {
        return Owner(
            type = Owner.OwnerType.NATURAL_PERSON,
            name = "Hennan",
            taxIdNumber = "87006987091"
        )
    }

    private fun createPixKeyResponse(): CreatePixKeyResponse {
        return CreatePixKeyResponse(
            keyType = PixKeyType.EMAIL,
            key = "hennantestes@mail.com",
            bankAccount = bankAccount(),
            owner = owner(),
            createdAt = LocalDateTime.now()
        )
    }


    @Factory
    class Clients {

        @Singleton
        fun gerarClienteGrpc(@GrpcChannel(GrpcServerChannel.NAME) grpcChannel: ManagedChannel)
                : ChavePixServiceGrpc.ChavePixServiceBlockingStub {

            return ChavePixServiceGrpc.newBlockingStub(grpcChannel)
        }
    }

    @MockBean(BacenClient::class)
    fun bacenClient(): BacenClient? {
        return Mockito.mock(BacenClient::class.java)
    }

    @MockBean(ContaClient::class)
    fun itauClient(): ContaClient? {
        return Mockito.mock(ContaClient::class.java)
    }

    private fun dadosDaContaResponse(): ContaResponse {

        return ContaResponse(
            tipo = "CONTA_CORRENTE",
            instituicao = InstituicaoResponse(nome = "UNIBANCO ITAU SA", ContaAssociada.ITAU_UNIBANCO_ISPB),
            agencia = "04055",
            numero = "1234",
            titular = TitularResponse(CLIENT_ID.toString(), nome = "hennan", cpf = "87006987091")
        )

    }

}



