package com.example.controlealimentar.gateway

import com.example.controlealimentar.gateway.data.AlimentoPaginadoResponseGateway
import com.example.controlealimentar.gateway.data.AlimentoResponseGateway
import com.example.controlealimentar.gateway.data.SalvarAlimentoRequestGateway
import retrofit2.Call
import retrofit2.http.*

interface AlimentoGateway {

    @GET("alimento")
    fun buscarAlimento(@Header("nomeAlimento") nomeAlimento : String) : Call<List<AlimentoResponseGateway>>

    @GET("alimento/paginado")
    fun buscarAlimentoPaginado(@Header("nomeAlimento") nomeAlimento : String,
                                @Query("size") size: Int,
                                @Query("page") page: Int) : Call<AlimentoPaginadoResponseGateway>

    @POST("alimento/salvar")
    fun salvarAlimento(@Header("idAlimento") idAlimento : String,
                       @Header("idRefeicao") idRefeicao : String,
                       @Header("processoId") processoId : String,
                       @Body body : SalvarAlimentoRequestGateway) : Call<Void>

    @PUT("alimento/registrar")
    fun consumirAlimento(@Header("processoId") processoId : String,
                         @Header("idRegistro") idRegistro : String) : Call<Void>
}