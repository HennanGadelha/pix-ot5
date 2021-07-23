package br.com.zup.pix.lista

import br.com.zup.*
import br.com.zup.pix.ChavePixRepository
import com.google.protobuf.Timestamp
import io.grpc.stub.StreamObserver
import java.lang.IllegalArgumentException
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ListaChavesEndpoint(@Inject private val chavePixRepository: ChavePixRepository)
    : ListarChavePixServiceGrpc.ListarChavePixServiceImplBase()  {


    override fun listar(request: ListarChavesPixRequest,
                        responseObserver: StreamObserver<ListarChavesPixResponse>) {

        if(request.clientId.isNullOrBlank())
            throw IllegalArgumentException("Identificado do cliente nao pode ser nulo ou vazio")


        var clientId = UUID.fromString(request.clientId)

        val chaves = chavePixRepository.findAllByClientId(clientId).map {

            ListarChavesPixResponse
                .ChavePix
                .newBuilder()
                .setPixId(it.id.toString())
                .setTipoDaChave(TipoChavePix.valueOf(it.tipo.name))
                .setChave(it.chave)
                .setTipoDaConta(TipoDaConta.valueOf(it.tipoDaconta.name)).setCriadaEm(it.criadaEm.let {

                    val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                    Timestamp.newBuilder()
                        .setSeconds(createdAt.epochSecond)
                        .setNanos(createdAt.nano)
                        .build()
                })
                .build()
        }

        responseObserver.onNext(
            ListarChavesPixResponse
                .newBuilder()
                .setClientId(clientId.toString()).addAllChaves(chaves)
                .build()
        )

        responseObserver.onCompleted()

    }

}