package br.com.zup.conta

import br.com.zup.instituicao.InstituicaoResponse
import br.com.zup.titular.TitularResponse

data class ContaResponse(
    val tipo: String,
    val instituicao: InstituicaoResponse,
    val agencia: String,
    val numero: String,
    val titular: TitularResponse
) {

    fun toModel() : ContaAssociada{
        return ContaAssociada(instituicao = this.instituicao.nome,
            nomeDoTitular = this.titular.nome,
            cpfDotitular = this.titular.cpf,
            agencia = this.agencia,
            numeroDaConta = this.numero)
    }
    
}