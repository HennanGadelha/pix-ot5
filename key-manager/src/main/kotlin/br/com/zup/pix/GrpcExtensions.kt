package br.com.zup.pix

import br.com.zup.CadastraChavePixRequest
import br.com.zup.TipoChavePix
import br.com.zup.TipoDaConta

fun CadastraChavePixRequest.toModel() : NovaChave {

    return NovaChave(
        clientId = clientId,
        tipo = when(tipoChavePix){
            TipoChavePix.CHAVE_DESCONHECIDA -> null
            else -> TipoDeChave.valueOf(tipoChavePix.name)
        },
        chave = chave,
        tipoDaConta = when(tipoDaConta) {
            TipoDaConta.TIPO_DE_CONTA_DESCONHECIDO -> null
            else -> TipoDaConta.valueOf((tipoDaConta.name))
        }
    )

}

