//package br.com.zup.servidor
//
//import br.com.zup.pix.ChavePixEndpoint
//import br.com.zup.pix.NovaChavePixService
//import io.grpc.ServerBuilder
//import javax.inject.Inject
//
//
//fun main() {
//
//
//    val server = ServerBuilder.forPort(50051)
//        .addService(ChavePixEndpoint())
//        .build()
//
//    server.start()
//    server.awaitTermination()
//}
//
