package br.com.zup.pix

import br.com.zup.handlers.ErrorAroundHandler
import br.com.zup.handlers.exceptions.ChavePixExistenteException
import br.com.zup.servicoexterno.ContaClient
import io.micronaut.validation.Validated
import java.lang.IllegalStateException
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid


@Validated
@Singleton
 class NovaChavePixService(@Inject val contaClient: ContaClient,
                          @Inject val chavePixRepository: ChavePixRepository) {


    @Transactional
      fun registraChave(@Valid novaChave: NovaChave): ChavePix{


        if(chavePixRepository.existsByChave(novaChave.chave)) {
            println("Chave: '${novaChave.chave}' já cadastrada")
            throw ChavePixExistenteException("Chave: '${novaChave.chave}' já cadastrada")
        }

        val response = contaClient.consultarConta(novaChave.clientId!!, novaChave.tipoDaConta!!.name)

        val conta = response.body()?.toModel() ?: throw IllegalStateException("Cliente nao encontrado")

        val chavePix = novaChave.toModel(conta)

        chavePixRepository.save(chavePix)

        println(chavePix)

        return  chavePix


    }

}