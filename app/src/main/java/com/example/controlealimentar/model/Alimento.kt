package com.example.controlealimentar.model

data class Alimento(
    val id: String = "",
    val nome: String = "",
    val calorias: Double = 0.0,
    val carboidratos: Double = 0.0,
    val proteinas: Double = 0.0,
    val gorduras: Double = 0.0,
    val porcao: Porcao
)