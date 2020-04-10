package com.example.controlealimentar.model

data class Refeicao(
    val id: String,
    val nome: String,
    val horario: String = "00:00",
    val caloria: Int = 0,
    val proteina: Int = 0,
    val carboidrato: Int = 0,
    val gordura: Int = 0
)