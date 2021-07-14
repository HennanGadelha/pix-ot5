package br.com.zup.pix

import br.com.zup.TipoChavePix
import br.com.zup.TipoDaConta
import br.com.zup.conta.ContaAssociada
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotBlank

@Introspected
data class NovaChave(
    @field:NotBlank
    val clientId :String?,
    @field:NotBlank
    val tipo : TipoChavePix?,
    @field:NotBlank
    val chave : String?,
    @field:NotBlank
    val tipoDaConta : TipoDaConta?) {


    fun toModel(conta: ContaAssociada): ChavePix{

        return ChavePix(clientId = UUID.fromString(this.clientId),
            tipo = TipoChavePix.valueOf(this.tipo!!.name),
            chave= if(this.tipo == TipoChavePix.ALEATORIA) UUID.randomUUID().toString() else this.chave!!,
            tipoDaconta= TipoDaConta.valueOf(this.tipoDaConta!!.name),
            conta = conta)

    }

}