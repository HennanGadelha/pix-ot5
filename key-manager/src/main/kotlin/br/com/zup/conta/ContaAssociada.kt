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

   companion object {
      public val ITAU_UNIBANCO_ISPB: String = "60701190"
   }

}