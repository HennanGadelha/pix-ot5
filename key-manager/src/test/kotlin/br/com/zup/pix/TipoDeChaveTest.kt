package br.com.zup.pix

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class TipoDeChaveTest{



    @Nested
    inner class CPF{

        @Test
        fun `deve cadastrar cpf valido`(){

            with(TipoDeChave.CPF){
                assertTrue(valida("40482776005"))
            }

        }

        @Test
        fun `nao deve cadastrar cpf invalido`(){

            with(TipoDeChave.CPF){
                assertFalse(valida("capivara"))
            }

        }

    }

    @Nested
    inner class CELULAR{

        @Test
        fun `deve cadastrar celular valido`(){

            with(TipoDeChave.CELULAR){
                assertTrue(valida("+5581985781620"))
            }

        }

        @Test
        fun `nao deve cadastrar celular invalido`(){

            with(TipoDeChave.CELULAR){
                assertFalse(valida("81985781620"))
            }

        }

    }

    @Nested
    inner class EMAIL{

        @Test
        fun `deve cadastrar email valido`(){

            with(TipoDeChave.EMAIL){
                assertTrue(valida("hennan.freitas@zup.com.br"))
            }

        }

        @Test
        fun `nao deve cadastrar celular invalido`(){

            with(TipoDeChave.EMAIL){
                assertFalse(valida("hennan"))
            }

        }

    }




    @Nested
    inner class ALEATORIA{

        @Test
        fun `deve ser valido chave aleatoria`(){

            with(TipoDeChave.ALEATORIA){
                assertTrue(valida(null))
            }
        }

        @Test
        fun `nao deve ser valido chave aleatoria`(){
            with(TipoDeChave.ALEATORIA){
                assertFalse(valida("chave"))
            }
        }

    }

}