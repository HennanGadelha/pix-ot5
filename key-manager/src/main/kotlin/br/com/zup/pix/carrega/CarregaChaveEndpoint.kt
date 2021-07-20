//package br.com.zup.pix.carrega
//
//import br.com.zup.CarregaChavePixRequest
//import br.com.zup.CarregaChavePixResponse
//import br.com.zup.CarregaChavePixServiceGrpc
//import br.com.zup.pix.ChavePixRepository
//import io.grpc.stub.StreamObserver
//import javax.inject.Inject
//import javax.inject.Singleton
//
//@Singleton
//class CarregaChaveEndpoint(@Inject val chavePixRepository: ChavePixRepository)
//    : CarregaChavePixServiceGrpc.CarregaChavePixServiceImplBase {
//
//
//    override fun carrega(
//        request: CarregaChavePixRequest?,
//        responseObserver: StreamObserver<CarregaChavePixResponse>
//    ) {
//
//
//
//    }
//
//
//}