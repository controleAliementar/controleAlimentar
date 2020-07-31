package com.example.controlealimentar.model


data class SalvarAlimento(
    val porcaoConsumida: Double = 0.0,
    val idPorcao: String? = null,
    val calorias: Double = 0.0,
    val carboidratos: Double = 0.0,
    val proteinas: Double = 0.0,
    val gorduras: Double = 0.0,
    val alimentoIngerido : Boolean = false,
    var unidadePorcao: String? = null,
    var caloriasPorcao: Double? = null,
    var carboidratosPorcao: Double? = null,
    var proteinasPorcao: Double? = null,
    var gordurasPorcao: Double? = null,
    var nomeAlimento: String? = null,
    var porcaoAlimento: Double? = null
)
