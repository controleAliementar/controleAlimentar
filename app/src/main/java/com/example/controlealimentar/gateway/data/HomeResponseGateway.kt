package com.example.controlealimentar.gateway.data

class HomeResponseGateway {
    val metas: MetasResponseGateway = MetasResponseGateway()
    val refeicoes: List<RefeicoesResponseGateway> = listOf(RefeicoesResponseGateway())

    class MetasResponseGateway{
        val id: String = ""
        val processoId: String = ""
        val calorias: Double = 0.0
        val caloriasConsumidas: Double = 0.0
        val carboidratos: Double = 0.0
        val carboidratosConsumidos: Double = 0.0
        val gorduras: Double = 0.0
        val gordurasConsumidas: Double = 0.0
        val proteinas: Double = 0.0
        val proteinasConsumidas: Double = 0.0
    }

    class RefeicoesResponseGateway{
        val id: String = ""
        val nome: String = ""
        val posicao: Int = 0
        val calorias: Double = 0.0
        val carboidratos: Double = 0.0
        val proteinas: Double = 0.0
        val gorduras: Double = 0.0
        val horaConsumo: Long = 0

    }
}

