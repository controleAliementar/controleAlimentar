package com.example.controlealimentar.model


data class AlimentoDetalhado(
    val idRegistro: String = "",
    val idAlimento: String = "",
    val nomeAlimento: String = "",
    val calorias: Double = 0.0,
    val carboidratos: Double = 0.0,
    val proteinas: Double = 0.0,
    val gorduras: Double = 0.0,
    val porcaoConsumida: Double = 0.0,
    val alimentoIngerido: Boolean = false
)