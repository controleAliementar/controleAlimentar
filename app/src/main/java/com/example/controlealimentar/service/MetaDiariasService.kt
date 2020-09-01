package com.example.controlealimentar.service

import android.util.Log
import com.example.controlealimentar.config.RetrofitConfig
import com.example.controlealimentar.exception.BuscarMetaDiariasException
import com.example.controlealimentar.exception.MetaDiariaNaoPodeSerEditadaException
import com.example.controlealimentar.exception.SalvarMetaDiariasException
import com.example.controlealimentar.gateway.data.MetaDiariasHistoricoResponseGateway
import com.example.controlealimentar.gateway.data.MetaDiariasRequestGateway
import com.example.controlealimentar.gateway.data.MetaDiariasResponseGateway
import com.example.controlealimentar.model.MetaDiarias
import com.example.controlealimentar.model.MetaDiariasHistorico
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MetaDiariasService {

    private val retrofitConfig: RetrofitConfig = RetrofitConfig()

    fun buscarMetaDiarias(processoId: String,
                          onSuccess : (MetaDiarias) -> Unit,
                          onError : (Exception) -> Unit) {

        val call = retrofitConfig.getMetaDiariasGateway()!!
            .buscarMetaDiarias(processoId)

        call.enqueue(object : Callback<MetaDiariasResponseGateway> {
            override fun onResponse(call: Call<MetaDiariasResponseGateway>,
                                    response: Response<MetaDiariasResponseGateway>
            ) {
                if (!response.isSuccessful){
                    print(response.errorBody())
                    return onError(BuscarMetaDiariasException(response.message()))
                } else if (response.code() == 204){
                    return onSuccess(MetaDiarias())
                }

                val metaDiarias = MetaDiarias()
                metaDiarias.id = response.body()!!.id
                metaDiarias.processoId = response.body()!!.processoId
                metaDiarias.calorias = response.body()!!.calorias
                metaDiarias.carboidratos = response.body()!!.carboidratos
                metaDiarias.proteinas = response.body()!!.proteinas
                metaDiarias.gorduras = response.body()!!.gorduras
                metaDiarias.dataInclusao = response.body()!!.dataInclusao

                onSuccess(metaDiarias)
            }

            override fun onFailure(call: Call<MetaDiariasResponseGateway>, t: Throwable?) {
                Log.e("Deu ruim: ", t?.message)
                onError(BuscarMetaDiariasException(t?.message))
            }
        })

    }

    fun salvarMetaDiarias(processoId: String,
                          metaDiarias: MetaDiarias,
                          onSuccess : () -> Unit,
                          onError : (Exception) -> Unit,
                          onErrorTratado : (Exception) -> Unit) {

        val metaDiariasRequestGateway = MetaDiariasRequestGateway(
            metaDiarias.calorias,
            metaDiarias.carboidratos,
            metaDiarias.gorduras,
            metaDiarias.proteinas
        )

        val call = retrofitConfig.getMetaDiariasGateway()!!
            .salvarMetaDiarias(processoId, metaDiariasRequestGateway)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>,
                                    response: Response<Void>
            ) {
                if (!response.isSuccessful){

                    if (response.code() == 412){
                        return onErrorTratado(MetaDiariaNaoPodeSerEditadaException(response.message()))
                    }
                    print(response.errorBody())
                    return onError(SalvarMetaDiariasException(response.message()))
                }

                onSuccess()
            }

            override fun onFailure(call: Call<Void>, t: Throwable?) {
                Log.e("Deu ruim: ", t?.message)
                onError(SalvarMetaDiariasException(t?.message))
            }
        })

    }

    fun buscarHistoricoMetaDiarias(processoId: String,
                                   dataInicio: String,
                                   dataFim: String,
                                   onSuccess : (List<MetaDiariasHistorico>) -> Unit,
                                   onError : (Exception) -> Unit) {

        val call = retrofitConfig.getMetaDiariasGateway()!!
            .buscarHistoricoMetaDiarias(processoId, dataFim, dataInicio)

        call.enqueue(object : Callback<List<MetaDiariasHistoricoResponseGateway>> {
            override fun onResponse(call: Call<List<MetaDiariasHistoricoResponseGateway>>,
                                    response: Response<List<MetaDiariasHistoricoResponseGateway>>
            ) {
                if (!response.isSuccessful){
                    print(response.errorBody())
                    return onError(BuscarMetaDiariasException(response.message()))
                } else if (response.code() == 204){
                    return onSuccess(emptyList())
                }

                val listHistoricoMetaDiariaGateway = response.body()
                val listHistoricoMetaDiaria : ArrayList<MetaDiariasHistorico> = arrayListOf()

                listHistoricoMetaDiariaGateway!!.forEach {

                    val metaDiariaHistorico = MetaDiariasHistorico()
                    metaDiariaHistorico.calorias = it.calorias
                    metaDiariaHistorico.caloriasConsumidas = it.caloriasConsumidas
                    metaDiariaHistorico.carboidratos = it.carboidratos
                    metaDiariaHistorico.carboidratosConsumidas = it.carboidratosConsumidos
                    metaDiariaHistorico.proteinas = it.proteinas
                    metaDiariaHistorico.proteinasConsumidas = it.proteinasConsumidas
                    metaDiariaHistorico.gorduras = it.gorduras
                    metaDiariaHistorico.gordurasConsumidas = it.gordurasConsumidas
                    metaDiariaHistorico.id = it.id
                    metaDiariaHistorico.processoId = it.processoId
                    metaDiariaHistorico.metaAtingida = it.metaAtingida
                    metaDiariaHistorico.dataReferencia = it.dataReferencia

                    listHistoricoMetaDiaria.add(metaDiariaHistorico)

                }

                onSuccess(listHistoricoMetaDiaria)
            }

            override fun onFailure(call: Call<List<MetaDiariasHistoricoResponseGateway>>, t: Throwable?) {
                Log.e("Deu ruim: ", t?.message)
                onError(BuscarMetaDiariasException(t?.message))
            }
        })

    }

}