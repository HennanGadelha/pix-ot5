package br.com.zup.pix.carrega

import br.com.zup.handlers.exceptions.ChaveNaoEncontradaException
import br.com.zup.pix.ChavePixRepository
import br.com.zup.servicoexterno.bacen.BacenClient
import io.micronaut.core.annotation.Introspected
import io.micronaut.http.HttpStatus
import net.bytebuddy.implementation.bytecode.Throw
import org.slf4j.LoggerFactory
import java.lang.IllegalArgumentException
import java.util.*

@Introspected
sealed class Filtro {


    abstract fun filtra(chavePixRepository: ChavePixRepository, bacenClient: BacenClient): DetalhesChavePix


    @Introspected
    data class FiltraPorPixId(val clienteId: String, val pixId: String) : Filtro() {

        fun clientIdAsUuid() = UUID.fromString(clienteId)
        fun pixIdAsUuid() = UUID.fromString(pixId)


        override fun filtra(chavePixRepository: ChavePixRepository, bacenClient: BacenClient): DetalhesChavePix {

            //chavePixRepository.findByIdAndClientId(pixIdAsUuid(), clientIdAsUuid())

            return chavePixRepository.findById(pixIdAsUuid())
                .filter { it.pertenceAo(clienteId) }
                .map(DetalhesChavePix::of)
                .orElseThrow { ChaveNaoEncontradaException("Chave Pix não encontrada") }

        }

    }


    @Introspected
    data class PorChave(val chave: String) : Filtro() {

        private val LOGGER = LoggerFactory.getLogger(this::class.java)

        override fun filtra(chavePixRepository: ChavePixRepository,
                            bacenClient: BacenClient): DetalhesChavePix {

            return chavePixRepository.findByChave(chave)
                .map(DetalhesChavePix::of).orElseGet {
                    LOGGER.info("Consultando chave Pix '$chave' no Bacen")

                    val response = bacenClient.buscarChavePix(chave)
                    when (response.status) {
                        HttpStatus.OK -> response.body()?.toModel()
                        else -> throw ChaveNaoEncontradaException("Chave Pix não encontrada")
                    }
                }

        }
    }
}