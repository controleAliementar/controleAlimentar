package com.example.controlealimentar.service

import android.util.Log
import com.example.controlealimentar.config.RetrofitConfig
import com.example.controlealimentar.exception.BuscarHomeException
import com.example.controlealimentar.gateway.data.HomeResponseGateway
import com.example.controlealimentar.model.Home
import com.example.controlealimentar.model.Refeicao
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeService {

    private val retrofitConfig: RetrofitConfig = RetrofitConfig()

    fun buscarHome(processoId: String,
                                        onSuccess : (Home) -> Unit,
                                        onError : (Exception) -> Unit) {

        val call = retrofitConfig.getHomeGateway()!!
            .buscarHome(processoId)

        call.enqueue(object : Callback<HomeResponseGateway> {
            override fun onResponse(call: Call<HomeResponseGateway>,
                                    response: Response<HomeResponseGateway>
            ) {
                if (!response.isSuccessful){
                    print(response.errorBody())
                    return onError(BuscarHomeException(response.message()))
                }

                val homeGateway = response.body()

                val home = Home()

                home.calorias = homeGateway?.metas?.calorias ?: 0.0
                home.caloriasConsumidas = homeGateway?.metas?.caloriasConsumidas ?: 0.0
                home.carboidratos = homeGateway?.metas?.carboidratos ?: 0.0
                home.carboidratosConsumidos = homeGateway?.metas?.carboidratosConsumidos ?: 0.0
                home.proteinas = homeGateway?.metas?.proteinas ?: 0.0
                home.proteinasConsumidas = homeGateway?.metas?.proteinasConsumidas ?: 0.0
                home.gorduras = homeGateway?.metas?.gorduras ?: 0.0
                home.gordurasConsumidas = homeGateway?.metas?.gordurasConsumidas ?: 0.0


                val listRefeicaoConsolidado: ArrayList<Refeicao> = arrayListOf()
                homeGateway!!.refeicoes.forEach {
                    val refeicaoConsolidado = Refeicao(
                        it.id,
                        it.nome,
                        it.horaConsumo,
                        it.calorias,
                        it.proteinas,
                        it.carboidratos,
                        it.gorduras
                    )

                    listRefeicaoConsolidado.add(refeicaoConsolidado)
                }

                home.refeicoes = listRefeicaoConsolidado

                onSuccess(home)
            }

            override fun onFailure(call: Call<HomeResponseGateway>, t: Throwable?) {
                Log.e("Deu ruim: ", t?.message)
                onError(BuscarHomeException(t?.message))
            }
        })

    }

}