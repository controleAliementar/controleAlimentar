package com.example.controlealimentar.service

import android.util.Log
import com.example.controlealimentar.config.RetrofitConfig
import com.example.controlealimentar.exception.AlterarHorarioRefeicaoException
import com.example.controlealimentar.exception.BuscarAlimentosRefeicaoException
import com.example.controlealimentar.exception.BuscarRefeicaoConsolidadaException
import com.example.controlealimentar.gateway.data.HorarioRefeicaoRequestGateway
import com.example.controlealimentar.gateway.data.RefeicaoAlimentosResponseGateway
import com.example.controlealimentar.gateway.data.RefeicaoConsolidadoResponseGateway
import com.example.controlealimentar.model.AlimentoDetalhado
import com.example.controlealimentar.model.Refeicao
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RefeicaoService {

    private val retrofitConfig: RetrofitConfig = RetrofitConfig()

    fun buscarListaRefeicoesConsolidado(processoId: String,
                                        onSuccess : (ArrayList<Refeicao>) -> Unit,
                                        onError : (Exception) -> Unit) {

        val call = retrofitConfig.getRefeicaoGateway()!!
            .buscarRefeicaoConsolidado(processoId)

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
                        it.horaConsumo,
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

    fun buscarRefeicaoAlimentos(processoId: String,
                                refeicaoId: String,
                                onSuccess : (ArrayList<AlimentoDetalhado>) -> Unit,
                                onError : (Exception) -> Unit) {

        val call = retrofitConfig.getRefeicaoGateway()!!
            .buscarAlimentosRefeicao(processoId, refeicaoId)

        call.enqueue(object : Callback<List<RefeicaoAlimentosResponseGateway>> {
            override fun onResponse(call: Call<List<RefeicaoAlimentosResponseGateway>>,
                                    response: Response<List<RefeicaoAlimentosResponseGateway>>
            ) {
                if (!response.isSuccessful){
                    print(response.errorBody())
                    return onError(BuscarAlimentosRefeicaoException(response.message()))
                } else if (response.code() == 204){
                    return onSuccess(arrayListOf())
                } else {

                    val refeicaoAlimentosResponseGateway = response.body()

                    val listAlimentosRefeicao: ArrayList<AlimentoDetalhado> = arrayListOf()

                    refeicaoAlimentosResponseGateway!!.forEach {
                        val alimentoDetalhado = AlimentoDetalhado(
                            it.idRegistro,
                            it.idAlimento,
                            it.nomeAlimento,
                            it.calorias,
                            it.carboidratos,
                            it.proteinas,
                            it.gorduras,
                            it.porcaoConsumida,
                            it.alimentoIngerido
                        )

                        listAlimentosRefeicao.add(alimentoDetalhado)
                    }

                    onSuccess(listAlimentosRefeicao)
                }

            }

            override fun onFailure(call: Call<List<RefeicaoAlimentosResponseGateway>>, t: Throwable?) {
                Log.e("Deu ruim: ", t?.message)
                onError(BuscarAlimentosRefeicaoException(t?.message))
            }
        })

    }

    fun alterarHorarioRefeicao(processoId: String,
                               refeicaoId: String,
                               horario: Long,
                               onSuccess : () -> Unit,
                               onError : (Exception) -> Unit) {

        val horarioRequest = HorarioRefeicaoRequestGateway(horario)

        val call = retrofitConfig.getRefeicaoGateway()!!
            .alterarHorarioRefeicao(processoId, refeicaoId, horarioRequest)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>,
                                    response: Response<Void>
            ) {
                if (!response.isSuccessful){
                    print(response.errorBody())
                    return onError(AlterarHorarioRefeicaoException(response.message()))
                }

                onSuccess()
            }

            override fun onFailure(call: Call<Void>, t: Throwable?) {
                Log.e("Deu ruim: ", t?.message)
                onError(AlterarHorarioRefeicaoException(t?.message))
            }
        })

    }

}