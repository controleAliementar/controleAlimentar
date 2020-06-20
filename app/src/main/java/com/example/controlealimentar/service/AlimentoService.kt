package com.example.controlealimentar.service

import android.util.Log
import com.example.controlealimentar.config.RetrofitConfig
import com.example.controlealimentar.exception.*
import com.example.controlealimentar.gateway.data.AlimentoPaginadoResponseGateway
import com.example.controlealimentar.gateway.data.BuscarAlimentoPorIdResponseGateway
import com.example.controlealimentar.gateway.data.EditarAlimentoRequestGateway
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

    fun buscarAlimentoPorId(idAlimento: String,
                            idRegistro: String,
                            processoId: String,
                            onSuccess : (Alimento) -> Unit,
                            onError : (Exception) -> Unit){


        val call = retrofitConfig.getAlimentoGateway()!!
            .buscarAlimentoPorId(processoId, idRegistro, idAlimento)

        call.enqueue(object : Callback<BuscarAlimentoPorIdResponseGateway> {
            override fun onResponse(call: Call<BuscarAlimentoPorIdResponseGateway>,
                                    response: Response<BuscarAlimentoPorIdResponseGateway>
            ) {
                if (!response.isSuccessful){
                    print(response.errorBody())
                    return onError(BuscarAlimentoException(response.message()))
                }

                val response =  response.body()!!.alimentoBanco

                var porcao = Porcao()

                if (response.porcao != null){
                    porcao = Porcao(
                        response.porcao.id,
                        response.porcao.porcao,
                        response.porcao.qtdGramas
                    )
                }

                val alimento = Alimento(
                    response.id,
                    response.nome,
                    response.calorias,
                    response.carboidratos,
                    response.proteinas,
                    response.gorduras,
                    porcao
                )

                onSuccess(alimento)
            }

            override fun onFailure(call: Call<BuscarAlimentoPorIdResponseGateway>, t: Throwable?) {
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

    fun editarAlimento(alimento: SalvarAlimento,
                       idRegistro: String,
                       processoId: String,
                       onSuccess : () -> Unit,
                       onError : (Exception) -> Unit) {


        val editarAlimentoRequestGateway = EditarAlimentoRequestGateway(
            alimento.porcaoConsumida,
            alimento.idPorcao,
            alimento.calorias,
            alimento.carboidratos,
            alimento.proteinas,
            alimento.gorduras,
            alimento.alimentoIngerido
        )

        val call = retrofitConfig.getAlimentoGateway()!!
            .editarAlimento(idRegistro,
                processoId,
                editarAlimentoRequestGateway)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>,
                                    response: Response<Void>
            ) {
                if (!response.isSuccessful){
                    print(response.errorBody())
                    return onError(EditarAlimentoException(response.message()))
                }
                onSuccess()
            }

            override fun onFailure(call: Call<Void>, t: Throwable?) {
                Log.e("Deu ruim: ", t?.message)
                onError(EditarAlimentoException(t?.message))
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

    fun deletarAlimento(processoId: String,
                         idRegistro: String,
                         onSuccess : () -> Unit,
                         onError : (Exception) -> Unit) {

        val call = retrofitConfig.getAlimentoGateway()!!
            .deletarAlimento(processoId, idRegistro)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>,
                                    response: Response<Void>
            ) {
                if (!response.isSuccessful){
                    print(response.errorBody())
                    return onError(DeletarAlimentoException(response.message()))
                }
                onSuccess()
            }

            override fun onFailure(call: Call<Void>, t: Throwable?) {
                Log.e("Deu ruim: ", t?.message)
                onError(DeletarAlimentoException(t?.message))
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

}