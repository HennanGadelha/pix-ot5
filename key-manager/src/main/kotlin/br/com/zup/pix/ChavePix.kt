package br.com.zup.pix

import br.com.zup.TipoChavePix
import br.com.zup.TipoDaConta
import br.com.zup.conta.ContaAssociada
import java.time.LocalDateTime
import java.util.*
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import java.util.UUID as UUID1

@Entity
class ChavePix(val clientId: UUID1,
               @Enumerated(EnumType.STRING)
               val tipo: TipoChavePix,
               val chave: String?,
               @Enumerated(EnumType.STRING)
               val tipoDaconta: TipoDaConta,
               val conta: ContaAssociada) {

    @Id
    val id: UUID1? = null;
    val criadaEm : LocalDateTime = LocalDateTime.now()

    override fun toString(): String {
        return "ChavePix(clientId=$clientId, " +
                "tipo=$tipo, " +
                "chave=$chave, " +
                "tipoDaconta=$tipoDaconta, " +
                "conta=$conta, " +
                "id=$id, " +
                "criadaEm=$criadaEm)"
    }


}
