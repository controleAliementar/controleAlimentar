package com.example.controlealimentar.service

import com.example.controlealimentar.config.RetrofitConfig
import com.example.controlealimentar.exception.BuscarMetaDiariasException
import com.example.controlealimentar.exception.SalvarMetaDiariasException
import com.example.controlealimentar.gateway.data.MetaDiariasRequestGateway
import com.example.controlealimentar.model.MetaDiarias

class MetaDiariasService {

    private val retrofitConfig: RetrofitConfig = RetrofitConfig()

    fun buscarMetaDiarias(processoId: String) : MetaDiarias? {

        val response = retrofitConfig.getMetaDiariasGateway()!!
            .buscarMetaDiarias(processoId)
            .execute()

        if (!response!!.isSuccessful){
            print(response.errorBody())
            throw BuscarMetaDiariasException(response.message())
        }

        if (response.code() == 204){
            return MetaDiarias()
        }

        val metaDiarias = MetaDiarias()
        metaDiarias.id = response.body()!!.id
        metaDiarias.processoId = response.body()!!.processoId
        metaDiarias.calorias = response.body()!!.calorias
        metaDiarias.carboidratos = response.body()!!.carboidratos
        metaDiarias.proteinas = response.body()!!.proteinas
        metaDiarias.gorduras = response.body()!!.gorduras

        return metaDiarias
    }

    fun salvarMetaDiarias(processoId: String, metaDiarias: MetaDiarias) {

        val metaDiariasRequestGateway = MetaDiariasRequestGateway(
            metaDiarias.calorias,
            metaDiarias.carboidratos,
            metaDiarias.gorduras,
            metaDiarias.proteinas
        )

        val response = retrofitConfig.getMetaDiariasGateway()!!
            .salvarMetaDiarias(processoId, metaDiariasRequestGateway)
            .execute()

        if (!response!!.isSuccessful){
            print(response.errorBody())
            throw SalvarMetaDiariasException(response.message())
        }

    }

}