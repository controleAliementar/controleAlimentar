package com.example.controlealimentar.gateway

import com.example.controlealimentar.gateway.data.AtualizarUsuarioRequestGateway
import com.example.controlealimentar.gateway.data.FeedbackUsuarioResponseGateway
import com.example.controlealimentar.gateway.data.UsuarioRequestGateway
import com.example.controlealimentar.gateway.data.UsuarioResponseGateway
import retrofit2.Call
import retrofit2.http.*

interface UsuarioGateway {

    @POST("usuario")
    fun salvarUsuario(@Body usuario : UsuarioRequestGateway) : Call<UsuarioResponseGateway>

    @GET("usuario/feedback")
    fun buscarFeedbackUsuario(@Header("processoId") processoId : String) : Call<FeedbackUsuarioResponseGateway>

    @PUT("usuario/feedback")
    fun atualizarFeedbackUsuario(@Header("processoId") processoId : String) : Call<Void>

    @GET("usuario/email")
    fun buscarUsuarioPorEmail(@Header("email") email : String) : Call<UsuarioResponseGateway>

    @PUT("usuario")
    fun atualizarUsuario(@Header("processoId") processoId : String,
        @Body usuario : AtualizarUsuarioRequestGateway) : Call<Void>
}