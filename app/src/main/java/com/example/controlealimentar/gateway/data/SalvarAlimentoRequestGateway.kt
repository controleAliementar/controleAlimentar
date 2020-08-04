package com.example.controlealimentar.gateway.data

class SalvarAlimentoRequestGateway(
    val porcaoConsumida: Double,
    val idPorcao: String?,
    val calorias: Double,
    val carboidratos: Double,
    val proteinas: Double,
    val gorduras: Double,
    val alimentoIngerido : Boolean,
    val unidadePorcao: String?,
    val caloriasPorcao: Double? = null,
    val carboidratosPorcao: Double? = null,
    val proteinasPorcao: Double? = null,
    val gordurasPorcao: Double? = null,
    val nomeAlimento: String? = null,
    val porcaoAlimento: Double? = null)

