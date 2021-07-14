package br.com.zup.pix

import br.com.zup.CadastraChavePixRequest
import br.com.zup.CadastraChavePixResponse
import br.com.zup.ChavePixServiceGrpc
import br.com.zup.handlers.ErrorAroundHandler
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ErrorAroundHandler
class ChavePixEndpoint(private val contaService : NovaChavePixService)
    : ChavePixServiceGrpc.ChavePixServiceImplBase() {

    override fun cadastrar(
        request: CadastraChavePixRequest,
        responseObserver: StreamObserver<CadastraChavePixResponse>
    ) {

        val novaChave = request?.toModel()
        val chaveCriada = contaService.registraChave(novaChave)


        val response = CadastraChavePixResponse
            .newBuilder()
            .setClientId(request?.clientId)
            .setPixId(chaveCriada.id.toString())
            .build()

        responseObserver?.onNext(response)
        responseObserver?.onCompleted()


        println(novaChave.toString())
    }

}




