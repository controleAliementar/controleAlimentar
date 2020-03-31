package com.example.controlealimentar.gateway.data

class AlimentoResponseGateway {
    val id: String = ""
    val nome: String = ""
    val calorias: Double = 0.0
    val carboidratos: Double = 0.0
    val proteinas: Double = 0.0
    val gorduras: Double = 0.0
    val porcao = PorcaoResponseGateway()

    class PorcaoResponseGateway{
        val id: String = ""
        val porcao: String = ""
        val qtdGramas: Double = 0.0
    }
}

