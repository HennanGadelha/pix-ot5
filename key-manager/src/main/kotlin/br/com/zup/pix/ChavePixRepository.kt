package br.com.zup.pix

import br.com.zup.pix.carrega.DetalhesChavePix
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface ChavePixRepository : JpaRepository<ChavePix, UUID> {

    fun existsByChave(chave: String?): Boolean

    fun existsByIdAndClientId(uuidPixId: UUID?, uuidClientId: UUID?): Boolean

   // fun findByIdAndClientId(pixIdAsUuid: UUID?, clientIdAsUuid: UUID?)

    fun findByChave(chave: String): Optional<ChavePix>
    fun findAllByClientId(clientId: UUID): MutableList<ChavePix>

}