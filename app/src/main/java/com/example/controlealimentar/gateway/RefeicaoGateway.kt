package com.example.controlealimentar.gateway

import com.example.controlealimentar.gateway.data.RefeicaoConsolidadoResponseGateway
import com.example.controlealimentar.gateway.data.RefeicaoIdsResponseGateway
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface RefeicaoGateway {

    @GET("refeicao")
    fun buscarRefeicaoIds() : Call<List<RefeicaoIdsResponseGateway>>

    @GET("refeicao/consolidado")
    fun buscarRefeicaoConsolidado(@Header("processoId") processoId: String)
            : Call<List<RefeicaoConsolidadoResponseGateway>>

}