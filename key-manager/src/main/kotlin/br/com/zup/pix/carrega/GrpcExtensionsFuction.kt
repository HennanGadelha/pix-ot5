package br.com.zup.pix.carrega

import br.com.zup.CarregaChavePixRequest
import  br.com.zup.CarregaChavePixRequest.FiltroCase
import java.lang.IllegalArgumentException


fun CarregaChavePixRequest.toModel() : Filtro{

    val filtro = when(filtroCase!!) {
        FiltroCase.PIXID -> pixId.let {
            Filtro.FiltraPorPixId(clienteId = it.clientId, pixId = it.pixId)
        }
        FiltroCase.CHAVE -> Filtro.PorChave(chave) // 2
        FiltroCase.FILTRO_NOT_SET -> throw IllegalArgumentException("Informar apenas chavePix ou (clientId e pixId)")
    }

    return filtro
}