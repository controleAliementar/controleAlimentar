package com.example.controlealimentar.service

import com.example.controlealimentar.config.RetrofitConfig
import com.example.controlealimentar.exception.CadastrarUsuarioException
import com.example.controlealimentar.model.Refeicao

class RefeicaoService {

    private val retrofitConfig: RetrofitConfig = RetrofitConfig()

    fun buscarListaRefeicoesConsolidado(processoId: String) : ArrayList<Refeicao> {

        val response = retrofitConfig.getRefeicaoGateway()!!
            .buscarRefeicaoConsolidado(processoId)
            .execute()

        if (!response!!.isSuccessful){
            print(response.errorBody())
            throw CadastrarUsuarioException(response.message())
        }

        val listRefeicaoConsolidadoGateway = response.body()

        val listRefeicaoConsolidado: ArrayList<Refeicao> = arrayListOf()

        listRefeicaoConsolidadoGateway!!.forEach {
            val refeicaoConsolidado = Refeicao(
                it.id,
                it.nome,
                "00:00",
                it.calorias,
                it.proteinas,
                it.carboidratos,
                it.gorduras
            )

            listRefeicaoConsolidado.add(refeicaoConsolidado)
        }

        return listRefeicaoConsolidado
    }

}