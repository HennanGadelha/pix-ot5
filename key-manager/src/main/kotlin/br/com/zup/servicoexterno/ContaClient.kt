package br.com.zup.servicoexterno

import br.com.zup.conta.ContaResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client

//http://localhost:9091/
//"\${erp-itau.pix.url}"
@Client("http://localhost:9091/")
interface ContaClient {

    @Get("api/v1/clientes/{clienteId}/contas{?tipo}")
     fun consultarConta(@PathVariable clienteId : String, @QueryValue tipo: String): HttpResponse<ContaResponse>


}