package com.example.controlealimentar.gateway

import com.example.controlealimentar.gateway.data.HomeResponseGateway
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface HomeGateway {

    @GET("home")
    fun buscarHome(@Header("processoId") processoId : String) : Call<HomeResponseGateway>

}