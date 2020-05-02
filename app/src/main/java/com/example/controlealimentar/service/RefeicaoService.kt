package com.example.controlealimentar.service

import android.util.Log
import com.example.controlealimentar.config.RetrofitMockConfig
import com.example.controlealimentar.exception.BuscarRefeicaoConsolidadaException
import com.example.controlealimentar.gateway.data.RefeicaoConsolidadoResponseGateway
import com.example.controlealimentar.model.Refeicao
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RefeicaoService {

//    private val retrofitConfig: RetrofitConfig = RetrofitConfig()
    private val retrofitConfig: RetrofitMockConfig = RetrofitMockConfig()

    fun buscarListaRefeicoesConsolidado(processoId: String,
                                        onSuccess : (ArrayList<Refeicao>) -> Unit,
                                        onError : (Exception) -> Unit) {

        val call = retrofitConfig.getRefeicaoGateway()!!
            .buscarRefeicaoConsolidadoMock(processoId)

        call.enqueue(object : Callback<List<RefeicaoConsolidadoResponseGateway>> {
            override fun onResponse(call: Call<List<RefeicaoConsolidadoResponseGateway>>,
                                    response: Response<List<RefeicaoConsolidadoResponseGateway>>
            ) {
                if (!response.isSuccessful){
                    print(response.errorBody())
                    return onError(BuscarRefeicaoConsolidadaException(response.message()))
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

                onSuccess(listRefeicaoConsolidado)
            }

            override fun onFailure(call: Call<List<RefeicaoConsolidadoResponseGateway>>, t: Throwable?) {
                Log.e("Deu ruim: ", t?.message)
                onError(BuscarRefeicaoConsolidadaException(t?.message))
            }
        })

    }

}