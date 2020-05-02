package com.example.controlealimentar.gateway

import com.example.controlealimentar.gateway.data.RefeicaoConsolidadoResponseGateway
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface RefeicaoGateway {

    @GET("refeicao/consolidado")
    fun buscarRefeicaoConsolidado(@Header("processoId") processoId: String)
            : Call<List<RefeicaoConsolidadoResponseGateway>>

    @GET("5eaca41d330000b483dfe65c")
    fun buscarRefeicaoConsolidadoMock(@Header("processoId") processoId: String)
            : Call<List<RefeicaoConsolidadoResponseGateway>>

}