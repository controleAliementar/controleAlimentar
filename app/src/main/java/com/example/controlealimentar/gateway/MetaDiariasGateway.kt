package com.example.controlealimentar.gateway

import com.example.controlealimentar.gateway.data.MetaDiariasRequestGateway
import com.example.controlealimentar.gateway.data.MetaDiariasResponseGateway
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface MetaDiariasGateway {

    @POST("meta/diaria")
    fun salvarMetaDiarias(@Header("processoId") processoId : String,
                         @Body metaDiarias : MetaDiariasRequestGateway) : Call<Void>

    @GET("meta/diaria")
    fun buscarMetaDiarias(@Header("processoId") processoId : String) : Call<MetaDiariasResponseGateway>
}