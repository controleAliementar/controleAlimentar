package com.example.controlealimentar.gateway

import com.example.controlealimentar.gateway.data.AlimentoResponseGateway
import com.example.controlealimentar.gateway.data.SalvarAlimentoRequestGateway
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AlimentoGateway {

    @GET("alimento")
    fun buscarAlimento(@Header("nomeAlimento") nomeAlimento : String) : Call<List<AlimentoResponseGateway>>

    @POST("alimento/salvar")
    fun salvarAlimento(@Header("idAlimento") idAlimento : String,
                       @Header("idRefeicao") idRefeicao : String,
                       @Header("processoId") processoId : String,
                       @Body body : SalvarAlimentoRequestGateway) : Call<Void>
}