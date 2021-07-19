package br.com.zup.pix.remove

import br.com.zup.handlers.exceptions.ChaveNaoEncontradaException
import br.com.zup.pix.ChavePixRepository
import io.micronaut.validation.Validated
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.constraints.NotBlank

@Validated
@Singleton
class RemoveChavePixService(@Inject val chavePixRepository: ChavePixRepository) {


    @Transactional
    fun removeChavePix(@NotBlank clientId: String?, @NotBlank pixId: String?){

        val uuidClientId = UUID.fromString(clientId)
        val uuidPixId = UUID.fromString(pixId)

        val existeChave = chavePixRepository.existsByIdAndClientId(uuidPixId, uuidClientId)

       if(!existeChave)  throw ChaveNaoEncontradaException("Chave pix nao encontrado ou nao pertence ao cliente")

        chavePixRepository.deleteById(uuidPixId)

    }

}
