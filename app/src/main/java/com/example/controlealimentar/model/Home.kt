package com.example.controlealimentar.model

data class Home(
    var calorias: Double = 0.0,
    var caloriasConsumidas: Double = 0.0,
    var carboidratos: Double = 0.0,
    var carboidratosConsumidos: Double = 0.0,
    var gorduras: Double = 0.0,
    var gordurasConsumidas: Double = 0.0,
    var proteinas: Double = 0.0,
    var proteinasConsumidas: Double = 0.0,
    var refeicoes: List<Refeicao> = emptyList()
)