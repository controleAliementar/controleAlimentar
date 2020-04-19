package com.example.controlealimentar.gateway.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class AlimentoPaginadoResponseGateway {
    val content: ArrayList<Content> = arrayListOf()
    val last: Boolean = false

    @JsonIgnoreProperties(ignoreUnknown = true)
    class Content{
        val id: String = ""
        val nome: String = ""
        val calorias: Double = 0.0
        val carboidratos: Double = 0.0
        val proteinas: Double = 0.0
        val gorduras: Double = 0.0
        val porcao = PorcaoResponseGateway()

        @JsonIgnoreProperties(ignoreUnknown = true)
        class PorcaoResponseGateway{
            val id: String = ""
            val porcao: String = ""
            val qtdGramas: Double = 0.0
        }

    }
}

