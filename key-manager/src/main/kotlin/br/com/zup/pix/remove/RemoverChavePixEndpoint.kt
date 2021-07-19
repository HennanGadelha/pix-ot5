package br.com.zup.pix.remove

import br.com.zup.RemoveChavePixRequest
import br.com.zup.RemoveChavePixResponse
import br.com.zup.RemoveChavePixServiceGrpc
import br.com.zup.handlers.ErrorAroundHandler
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional

@Singleton
@ErrorAroundHandler
class RemoverChavePixEndpoint(@Inject private val service : RemoveChavePixService)
    : RemoveChavePixServiceGrpc.RemoveChavePixServiceImplBase() {

    override fun remove(
        request: RemoveChavePixRequest,
        responseObserver: StreamObserver<RemoveChavePixResponse> ) {

        service.removeChavePix(clientId = request.clientId, pixId = request.pixId)

        responseObserver.onNext(RemoveChavePixResponse.newBuilder()
            .setClientId(request.clientId)
            .setPixId(request.pixId)
            .build())

        responseObserver.onCompleted()

    }



}