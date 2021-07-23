package br.com.zup.pix.carrega

import br.com.zup.CarregaChavePixResponse
import br.com.zup.TipoChavePix
import br.com.zup.TipoDaConta
import com.google.protobuf.Timestamp
import java.time.ZoneId

class CarregaChavePixResponseConverter {

    fun converter(detalhesChavePix: DetalhesChavePix) : CarregaChavePixResponse{

        return CarregaChavePixResponse.newBuilder()
            .setClientId(detalhesChavePix.clientId ?: "")
            .setPixId(detalhesChavePix.pixid ?: "")
            .setChave(CarregaChavePixResponse
                .ChavePixInfo
                .newBuilder()
                .setTipo(TipoChavePix.valueOf(detalhesChavePix.tipo.name))
                .setChave(detalhesChavePix.chave)
                .setConta(CarregaChavePixResponse
                    .ChavePixInfo
                    .DetalhesConta
                    .newBuilder()
                    .setTipo(TipoDaConta.valueOf(detalhesChavePix.tipoDeConta.name))
                    .setInstituicao(detalhesChavePix.conta.instituicao)
                    .setNomeDoTitular(detalhesChavePix.conta.nomeDoTitular)
                    .setCpfDoTitular(detalhesChavePix.conta.cpfDotitular)
                    .setAgencia(detalhesChavePix.conta.agencia)
                    .setNumeroDaConta(detalhesChavePix.conta.numeroDaConta))
                .setCriadaEm(detalhesChavePix.registradaEm.let {
                    val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                    Timestamp.newBuilder()
                        .setSeconds(createdAt.epochSecond)
                        .setNanos(createdAt.nano)
                        .build()
                }))
            .build()


    }



}