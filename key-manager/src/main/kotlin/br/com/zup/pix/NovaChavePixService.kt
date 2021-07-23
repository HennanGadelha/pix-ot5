package br.com.zup.pix

import br.com.zup.handlers.ErrorAroundHandler
import br.com.zup.handlers.exceptions.ChavePixExistenteException
import br.com.zup.handlers.exceptions.ClienteNaoExistenteExistenteException
import br.com.zup.servicoexterno.ContaClient
import br.com.zup.servicoexterno.bacen.BacenClient
import br.com.zup.servicoexterno.bacen.CreatePixKeyRequest
import io.micronaut.http.HttpStatus
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.lang.IllegalStateException
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid


@Validated
@Singleton
 class NovaChavePixService(@Inject val contaClient: ContaClient,
                           @Inject val bacenClient: BacenClient,
                           @Inject val chavePixRepository: ChavePixRepository) {


    val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
      fun registraChave(@Valid novaChave: NovaChave): ChavePix{


        if(chavePixRepository.existsByChave(novaChave.chave)) {
            println("Chave: '${novaChave.chave}' já cadastrada")
            throw ChavePixExistenteException("Chave: '${novaChave.chave}' já cadastrada")
        }

        val response = contaClient.consultarConta(novaChave.clientId!!, novaChave.tipoDaConta!!.name)

        val conta = response.body()?.toModel() ?: throw ClienteNaoExistenteExistenteException("Cliente nao encontrado")

        val chavePix = novaChave.toModel(conta)



        val bcbRequest = CreatePixKeyRequest.of(chavePix).also {
            logger.info("Regsitrando nova chave pix")
        }

        val bcbResponse = bacenClient.criarChavePix(bcbRequest)

        if(bcbResponse.status != HttpStatus.CREATED)
            throw IllegalStateException("Erro ao registrar chave pix no bacen")

        chavePix.chave = bcbResponse.body()!!.key


        chavePixRepository.save(chavePix)

        println(chavePix.id)

        return  chavePix

    }

}