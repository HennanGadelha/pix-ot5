package br.com.zup.pix.carrega

import br.com.zup.CarregaChavePixRequest
import br.com.zup.CarregaChavePixResponse
import br.com.zup.CarregaChavePixServiceGrpc
import br.com.zup.pix.ChavePixRepository
import br.com.zup.servicoexterno.bacen.BacenClient
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CarregaChaveEndpoint(@Inject val chavePixRepository: ChavePixRepository,
                           @Inject val bacenClient: BacenClient)
    : CarregaChavePixServiceGrpc.CarregaChavePixServiceImplBase() {


    override fun carrega(
        request: CarregaChavePixRequest,
        responseObserver: StreamObserver<CarregaChavePixResponse>
    ) {


        val filtro = request.toModel();
        val detalhesDaChave = filtro.filtra(chavePixRepository, bacenClient)


        responseObserver.onNext(CarregaChavePixResponseConverter().converter(detalhesChavePix = detalhesDaChave))
        responseObserver.onCompleted()




    }




}