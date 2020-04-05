package com.example.controlealimentar.model


data class SalvarAlimento(
    val porcaoConsumida: Double = 0.0,
    val calorias: Double = 0.0,
    val carboidratos: Double = 0.0,
    val proteinas: Double = 0.0,
    val gorduras: Double = 0.0,
    val alimentoIngerido : Boolean = false
)
