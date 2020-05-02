package com.example.controlealimentar.gateway

import com.example.controlealimentar.gateway.data.RefeicaoAlimentosResponseGateway
import com.example.controlealimentar.gateway.data.RefeicaoConsolidadoResponseGateway
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface RefeicaoGateway {

    @GET("refeicao/consolidado")
    fun buscarRefeicaoConsolidado(@Header("processoId") processoId: String)
            : Call<List<RefeicaoConsolidadoResponseGateway>>

    @GET("refeicao/alimento")
    fun buscarAlimentosRefeicao(@Header("processo_id") processoId: String,
                                @Header("id_refeicao") refeicaoId: String)
            : Call<List<RefeicaoAlimentosResponseGateway>>

    @GET("5ead98b82f000050001987f3")
    fun buscarRefeicaoConsolidadoMock(@Header("processoId") processoId: String)
            : Call<List<RefeicaoConsolidadoResponseGateway>>

}