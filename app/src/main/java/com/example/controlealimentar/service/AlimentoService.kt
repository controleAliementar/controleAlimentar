package com.example.controlealimentar.service

import com.example.controlealimentar.config.RetrofitConfig
import com.example.controlealimentar.exception.BuscarAlimentoException
import com.example.controlealimentar.gateway.data.AlimentoResponseGateway
import com.example.controlealimentar.model.Alimento
import com.example.controlealimentar.model.Porcao

class AlimentoService {

    private val retrofitConfig: RetrofitConfig = RetrofitConfig()

    fun buscarAlimento(nomeAlimento: String) : List<Alimento> {


        val response = retrofitConfig.getAlimentoGateway()!!
            .buscarAlimento(nomeAlimento)
            .execute()

        if (!response!!.isSuccessful){
            print(response.errorBody())
            throw BuscarAlimentoException(response.message())
        }

        val listAlimentoResponseGateway =  response.body()

        if (listAlimentoResponseGateway.isNullOrEmpty()){
            return emptyList()
        }

        val listAlimentos = getListAlimentos(listAlimentoResponseGateway)

        return listAlimentos
    }

    private fun getListAlimentos(listAlimentoResponseGateway: List<AlimentoResponseGateway>): List<Alimento> {
        val alimentoList: List<Alimento> = emptyList()

        listAlimentoResponseGateway.forEach {

            val porcao = Porcao(
                it.porcao.id,
                it.porcao.porcao,
                it.porcao.qtdGramas
            )

            val alimento = Alimento(
                it.id,
                it.nome,
                it.calorias,
                it.carboidratos,
                it.proteinas,
                it.gorduras,
                porcao
            )

            alimentoList.plus(alimento)
        }

        return alimentoList
    }

}