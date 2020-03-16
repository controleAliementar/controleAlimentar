package com.example.controlealimentar.gateway

import com.example.controlealimentar.gateway.data.UsuarioRequestGateway
import com.example.controlealimentar.gateway.data.UsuarioResponseGateway
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UsuarioGateway {

    @POST("usuario")
    fun salvarUsuario(@Body usuario : UsuarioRequestGateway) : Call<UsuarioResponseGateway>
}