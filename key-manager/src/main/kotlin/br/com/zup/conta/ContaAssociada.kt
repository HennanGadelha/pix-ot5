package br.com.zup.conta

import java.util.*
import javax.persistence.*

@Embeddable
class ContaAssociada(
   val instituicao: String,
   val nomeDoTitular: String,
   val cpfDotitular: String,
   val agencia: String,
   val numeroDaConta: String
) {


}