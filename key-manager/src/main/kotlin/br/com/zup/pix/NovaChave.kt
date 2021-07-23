package br.com.zup.pix

import br.com.zup.TipoChavePix
import br.com.zup.TipoDaConta
import br.com.zup.conta.ContaAssociada
import br.com.zup.handlers.validacoes.ValidPixKey
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@ValidPixKey
@Introspected
 data class NovaChave(
    @field:NotBlank
    val clientId :String?,
    @field:NotBlank
    @Enumerated(EnumType.STRING)
    val tipo : TipoDeChave?,
    val chave : String?,
    @field:NotBlank
    val tipoDaConta : TipoDaConta?) {


    fun toModel(conta: ContaAssociada): ChavePix{

        return ChavePix(clientId = UUID.fromString(this.clientId),
            tipo = TipoDeChave.valueOf(this.tipo!!.name),
            chave= if(this.tipo == TipoDeChave.ALEATORIA) UUID.randomUUID().toString() else this.chave!!,
            tipoDaconta= TipoDaConta.valueOf(this.tipoDaConta!!.name),
            conta = conta)

    }


}