package br.com.zup.pix

import br.com.zup.TipoChavePix
import br.com.zup.TipoDaConta
import br.com.zup.conta.ContaAssociada
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import java.util.UUID


@Entity
class ChavePix(val clientId : UUID,
               @Enumerated(EnumType.STRING)
               val tipo: TipoDeChave,
               var chave: String?,
               @Enumerated(EnumType.STRING)
               val tipoDaconta: TipoDaConta,
               //@ManyToOne(cascade= [CascadeType.PERSIST])
               @Embedded
               val conta: ContaAssociada) {

    @Id
    @GeneratedValue
    val id: UUID? = null;
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

    fun pertenceAo(clienteId: String) : Boolean{
        return this.clientId == clientId
    }


}
