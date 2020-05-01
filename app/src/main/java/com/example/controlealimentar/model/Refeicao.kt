package com.example.controlealimentar.model

data class Refeicao(
    val id: String,
    val nome: String,
    val horario: String = "00:00",
    val caloria: Double = 0.0,
    val proteina: Double = 0.0,
    val carboidrato: Double = 0.0,
    val gordura: Double = 0.0
)