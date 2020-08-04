package com.example.controlealimentar.gateway.data

class SalvarAlimentoUsuarioRequestGateway(
    val porcaoConsumida: Double,
    val nomeAlimento: String?,
    val calorias: Double,
    val porcaoAlimento: Double,
    val caloriaPorcao: Double,
    val carboidratos: Double,
    val carboidratoPorcao: Double,
    val proteinas: Double,
    val proteinaPorcao: Double,
    val gorduras: Double,
    val gorduraPorcao: Double,
    val alimentoIngerido : Boolean,
    val unidadePorcao : String)

