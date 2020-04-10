package com.example.controlealimentar.service

import com.example.controlealimentar.config.RetrofitConfig
import com.example.controlealimentar.exception.BuscarAlimentoException
import com.example.controlealimentar.gateway.data.AlimentoResponseGateway
import com.example.controlealimentar.gateway.data.SalvarAlimentoRequestGateway
import com.example.controlealimentar.model.Alimento
import com.example.controlealimentar.model.Porcao
import com.example.controlealimentar.model.SalvarAlimento

class AlimentoService {

    private val retrofitConfig: RetrofitConfig = RetrofitConfig()

    fun buscarAlimento(nomeAlimento: String) : ArrayList<Alimento> {


        val response = retrofitConfig.getAlimentoGateway()!!
            .buscarAlimento(nomeAlimento)
            .execute()

        if (!response!!.isSuccessful){
            print(response.errorBody())
            throw BuscarAlimentoException(response.message())
        }

        val listAlimentoResponseGateway =  response.body()

        if (listAlimentoResponseGateway.isNullOrEmpty()){
            return arrayListOf()
        }

        return getListAlimentos(listAlimentoResponseGateway)
    }

    fun salvarAlimento(alimento: SalvarAlimento,
                       idAlimento: String,
                       idRefeicao: String,
                       processoId: String) {


        val salvarAlimentoRequestGateway = SalvarAlimentoRequestGateway(
            alimento.porcaoConsumida,
            alimento.calorias,
            alimento.carboidratos,
            alimento.proteinas,
            alimento.gorduras,
            alimento.alimentoIngerido
        )


        val response = retrofitConfig.getAlimentoGateway()!!
            .salvarAlimento(idAlimento,
                            idRefeicao,
                            processoId,
                            salvarAlimentoRequestGateway)
            .execute()

        if (!response!!.isSuccessful){
            print(response.errorBody())
            throw BuscarAlimentoException(response.message())
        }

    }

    private fun getListAlimentos(listAlimentoResponseGateway: List<AlimentoResponseGateway>): ArrayList<Alimento> {
        val alimentoList: ArrayList<Alimento> = arrayListOf()

        listAlimentoResponseGateway.forEach {

            var porcao = Porcao()

            if (it.porcao != null){
                porcao = Porcao(
                    it.porcao.id,
                    it.porcao.porcao,
                    it.porcao.qtdGramas
                )
            }

            val alimento = Alimento(
                it.id,
                it.nome,
                it.calorias,
                it.carboidratos,
                it.proteinas,
                it.gorduras,
                porcao
            )

            alimentoList.add(alimento)
        }

        return alimentoList
    }

}