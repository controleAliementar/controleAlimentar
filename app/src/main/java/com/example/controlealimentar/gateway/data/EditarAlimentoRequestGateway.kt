package com.example.controlealimentar.gateway.data

class EditarAlimentoRequestGateway(
    val porcaoConsumida: Double,
    val idPorcao: String?,
    val calorias: Double,
    val carboidratos: Double,
    val proteinas: Double,
    val gorduras: Double,
    val alimentoIngerido : Boolean)

