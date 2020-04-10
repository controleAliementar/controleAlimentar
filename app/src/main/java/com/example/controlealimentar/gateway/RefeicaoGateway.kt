package com.example.controlealimentar.gateway

import com.example.controlealimentar.gateway.data.RefeicaoIdsResponseGateway
import retrofit2.Call
import retrofit2.http.GET

interface RefeicaoGateway {

    @GET("refeicao")
    fun buscarRefeicaoIds() : Call<List<RefeicaoIdsResponseGateway>>

}