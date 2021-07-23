package br.com.zup.pix.carrega

import br.com.zup.TipoChavePix
import br.com.zup.TipoDaConta
import br.com.zup.conta.ContaAssociada
import br.com.zup.pix.ChavePix
import br.com.zup.pix.TipoDeChave
import java.time.LocalDateTime

class DetalhesChavePix(
    val pixid: String? ="",
    val clientId: String? = "",
    val tipo: TipoDeChave,
    val chave: String,
    val tipoDeConta: TipoDaConta,
    val conta: ContaAssociada,
    val registradaEm: LocalDateTime = LocalDateTime.now()
) {

    companion object{

        fun of(chave: ChavePix): DetalhesChavePix{

            return DetalhesChavePix(
                pixid = chave.id.toString(),
                clientId = chave.clientId.toString(),
                tipo = chave.tipo,
                chave = chave.chave.toString(),
                tipoDeConta = chave.tipoDaconta,
                conta = chave.conta,
                registradaEm = chave.criadaEm
            )
        }
    }

}
