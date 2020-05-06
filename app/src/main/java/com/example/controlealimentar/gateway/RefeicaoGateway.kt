package com.example.controlealimentar.gateway

import com.example.controlealimentar.gateway.data.HorarioRefeicaoRequestGateway
import com.example.controlealimentar.gateway.data.RefeicaoAlimentosResponseGateway
import com.example.controlealimentar.gateway.data.RefeicaoConsolidadoResponseGateway
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT

interface RefeicaoGateway {

    @GET("refeicao/consolidado")
    fun buscarRefeicaoConsolidado(@Header("processoId") processoId: String)
            : Call<List<RefeicaoConsolidadoResponseGateway>>

    @GET("refeicao/alimento")
    fun buscarAlimentosRefeicao(@Header("processoId") processoId: String,
                                @Header("idRefeicao") refeicaoId: String)
            : Call<List<RefeicaoAlimentosResponseGateway>>

    @PUT("refeicao/horario")
    fun alterarHorarioRefeicao(@Header("processoId") processoId: String,
                               @Header("idRefeicao") refeicaoId: String,
                               @Body horario: HorarioRefeicaoRequestGateway): Call<Void>

    @GET("5ead98b82f000050001987f3")
    fun buscarRefeicaoConsolidadoMock(@Header("processoId") processoId: String)
            : Call<List<RefeicaoConsolidadoResponseGateway>>

}