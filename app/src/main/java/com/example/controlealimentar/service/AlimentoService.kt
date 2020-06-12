package com.example.controlealimentar.service

import android.util.Log
import com.example.controlealimentar.config.RetrofitConfig
import com.example.controlealimentar.exception.BuscarAlimentoException
import com.example.controlealimentar.exception.ConsumirAlimentoException
import com.example.controlealimentar.exception.SalvarAlimentoException
import com.example.controlealimentar.gateway.data.AlimentoPaginadoResponseGateway
import com.example.controlealimentar.gateway.data.AlimentoResponseGateway
import com.example.controlealimentar.gateway.data.SalvarAlimentoRequestGateway
import com.example.controlealimentar.model.Alimento
import com.example.controlealimentar.model.AlimentoPaginado
import com.example.controlealimentar.model.Porcao
import com.example.controlealimentar.model.SalvarAlimento
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    fun buscarAlimentoPaginado(nomeAlimento: String,
                               page: Int,
                               onSuccess : (AlimentoPaginado) -> Unit,
                               onError : (Exception) -> Unit){

        val size = 10

        val call = retrofitConfig.getAlimentoGateway()!!
            .buscarAlimentoPaginado(nomeAlimento, size, page)

        call.enqueue(object : Callback<AlimentoPaginadoResponseGateway> {
            override fun onResponse(call: Call<AlimentoPaginadoResponseGateway>,
                                    response: Response<AlimentoPaginadoResponseGateway>
            ) {
                if (!response.isSuccessful){
                    print(response.errorBody())
                    return onError(BuscarAlimentoException(response.message()))
                }

                val alimentoPaginadoResponseGateway =  response.body()

                if (alimentoPaginadoResponseGateway!!.content.isNullOrEmpty()){
                    return onSuccess(AlimentoPaginado())
                }

                val listPaginadoAlimentos =
                    getListPaginadoAlimentos(alimentoPaginadoResponseGateway.content)

                onSuccess(AlimentoPaginado(listPaginadoAlimentos, alimentoPaginadoResponseGateway.last))
            }

            override fun onFailure(call: Call<AlimentoPaginadoResponseGateway>, t: Throwable?) {
                Log.e("Deu ruim: ", t?.message)
                onError(BuscarAlimentoException(t?.message))
            }
        })

    }

    fun salvarAlimento(alimento: SalvarAlimento,
                       idAlimento: String,
                       idRefeicao: String,
                       processoId: String,
                       onSuccess : () -> Unit,
                       onError : (Exception) -> Unit) {


        val salvarAlimentoRequestGateway = SalvarAlimentoRequestGateway(
            alimento.porcaoConsumida,
            alimento.idPorcao,
            alimento.calorias,
            alimento.carboidratos,
            alimento.proteinas,
            alimento.gorduras,
            alimento.alimentoIngerido
        )

        val call = retrofitConfig.getAlimentoGateway()!!
            .salvarAlimento(idAlimento,
                            idRefeicao,
                            processoId,
                            salvarAlimentoRequestGateway)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>,
                                    response: Response<Void>
            ) {
                if (!response.isSuccessful){
                    print(response.errorBody())
                    return onError(SalvarAlimentoException(response.message()))
                }
                onSuccess()
            }

            override fun onFailure(call: Call<Void>, t: Throwable?) {
                Log.e("Deu ruim: ", t?.message)
                onError(SalvarAlimentoException(t?.message))
            }
        })

    }

    fun consumirAlimento(processoId: String,
                         idRegistro: String,
                         registrar: Boolean,
                       onSuccess : () -> Unit,
                       onError : (Exception) -> Unit) {

        val call = retrofitConfig.getAlimentoGateway()!!
            .consumirAlimento(processoId, idRegistro, registrar)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>,
                                    response: Response<Void>
            ) {
                if (!response.isSuccessful){
                    print(response.errorBody())
                    return onError(ConsumirAlimentoException(response.message()))
                }
                onSuccess()
            }

            override fun onFailure(call: Call<Void>, t: Throwable?) {
                Log.e("Deu ruim: ", t?.message)
                onError(ConsumirAlimentoException(t?.message))
            }
        })

    }

    private fun getListPaginadoAlimentos(listAlimentoResponseGateway: List<AlimentoPaginadoResponseGateway.Content>): ArrayList<Alimento> {
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