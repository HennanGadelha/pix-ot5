package br.com.zup.pix

import br.com.zup.servicoexterno.ContaClient
import java.lang.IllegalStateException
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional

@Singleton
class NovaChavePixService(@Inject val contaClient: ContaClient,
                          @Inject val chavePixRepository: ChavePixRepository) {


    @Transactional
     fun registraChave(novaChave: NovaChave): ChavePix{



        val response = contaClient.consultarConta(novaChave.clientId!!, novaChave.tipoDaConta!!.name)

        val conta = response.body()?.toModel() ?: throw IllegalStateException("Cliente nao encontrado")

        val chavePix = novaChave.toModel(conta)

        chavePixRepository.save(chavePix)

        return  chavePix


    }

}