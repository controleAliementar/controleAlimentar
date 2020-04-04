package com.example.controlealimentar.gateway

import com.example.controlealimentar.gateway.data.AlimentoResponseGateway
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface AlimentoGateway {

    @GET("alimento")
    fun buscarAlimento(@Header("nomeAlimento") nomeAlimento : String) : Call<List<AlimentoResponseGateway>>
}