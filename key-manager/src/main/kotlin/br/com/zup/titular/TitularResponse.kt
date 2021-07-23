package br.com.zup.titular

import io.micronaut.core.annotation.Introspected


data class TitularResponse(
    val id: String,
    val nome: String,
    val cpf: String
){

}
