package br.com.zup.pix.remove

import br.com.zup.handlers.exceptions.ChaveNaoEncontradaException
import br.com.zup.pix.ChavePixRepository
import br.com.zup.servicoexterno.bacen.BacenClient
import br.com.zup.servicoexterno.bacen.DeletePixKeyRequest
import io.micronaut.http.HttpStatus
import io.micronaut.validation.Validated
import java.lang.IllegalStateException
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.constraints.NotBlank

@Validated
@Singleton
class RemoveChavePixService(@Inject val chavePixRepository: ChavePixRepository,
                            @Inject val bacenClient: BacenClient) {


    @Transactional
    fun removeChavePix(@NotBlank clientId: String?, @NotBlank pixId: String?){

        val uuidClientId = UUID.fromString(clientId)
        val uuidPixId = UUID.fromString(pixId)

        val existeChave = chavePixRepository.existsByIdAndClientId(uuidPixId, uuidClientId)

        if(!existeChave)  throw ChaveNaoEncontradaException("Chave pix nao encontrado ou nao pertence ao cliente")

        chavePixRepository.deleteById(uuidPixId)

        
        //buscando chave pix para delecao no bacen
        val chavePix = chavePixRepository.findById(uuidPixId).get()

        val valorDaChavePix = chavePix.chave.toString()

        val request = DeletePixKeyRequest(valorDaChavePix)

        val response = bacenClient.deletarChavePix(valorDaChavePix, request)

        if(response.status != HttpStatus.OK)
            throw IllegalStateException("Erro ao remover chave no bacen")

    }

}
