package com.example.controlealimentar.gateway

import com.example.controlealimentar.gateway.data.*
import retrofit2.Call
import retrofit2.http.*

interface AlimentoGateway {

    @GET("alimento")
    fun buscarAlimento(@Header("nomeAlimento") nomeAlimento : String) : Call<List<AlimentoResponseGateway>>

    @GET("alimento/paginado")
    fun buscarAlimentoPaginado(@Header("nomeAlimento") nomeAlimento : String,
                                @Query("size") size: Int,
                                @Query("page") page: Int) : Call<AlimentoPaginadoResponseGateway>

    @GET("alimento/buscar-por-id")
    fun buscarAlimentoPorId(@Header("processoId") processoId : String,
                               @Query("idRegistro ") idRegistro : String,
                               @Query("idAlimento ") idAlimento : String): Call<BuscarAlimentoPorIdResponseGateway>

    @POST("alimento/salvar")
    fun salvarAlimento(@Header("idAlimento") idAlimento : String,
                       @Header("idRefeicao") idRefeicao : String,
                       @Header("processoId") processoId : String,
                       @Body body : SalvarAlimentoRequestGateway) : Call<Void>

    @PATCH("alimento/editar")
    fun editarAlimento(@Header("idRegistro") idRegistro : String,
                       @Header("processoId") processoId : String,
                       @Body body : EditarAlimentoRequestGateway) : Call<Void>

    @PUT("alimento/registrar")
    fun consumirAlimento(@Header("processoId") processoId : String,
                         @Header("idRegistro") idRegistro : String,
                         @Header("registrarAlimento") registrar : Boolean) : Call<Void>

    @DELETE("alimento")
    fun deletarAlimento(@Header("processoId") processoId : String,
                         @Header("idRegistro") idRegistro : String) : Call<Void>
}