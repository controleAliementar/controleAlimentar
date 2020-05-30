package com.example.controlealimentar.gateway.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class RefeicaoAlimentosResponseGateway {
    val idRegistro: String = ""
    val idAlimento: String = ""
    val nomeAlimento: String = ""
    val calorias: Double = 0.0
    val carboidratos: Double = 0.0
    val proteinas: Double = 0.0
    val gorduras: Double = 0.0
    val porcaoConsumida: Double = 0.0
    val unidadePorcao: String = ""
    val alimentoIngerido: Boolean = false
}

