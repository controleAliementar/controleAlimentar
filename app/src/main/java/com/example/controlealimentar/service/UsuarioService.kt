package com.example.controlealimentar.service

import com.example.controlealimentar.config.RetrofitConfig
import com.example.controlealimentar.exception.CadastrarUsuarioException
import com.example.controlealimentar.gateway.data.UsuarioRequestGateway
import com.example.controlealimentar.model.Usuario

class UsuarioService {

    private val retrofitConfig: RetrofitConfig = RetrofitConfig()

    fun salvarUsuario(usuario: Usuario) : String? {

        val usuarioGateway = UsuarioRequestGateway(
            usuario.email,
            usuario.nome)

        val response = retrofitConfig.getUsuarioGateway()!!
            .salvarUsuario(usuarioGateway)
            .execute()

        if (!response!!.isSuccessful){
            print(response.errorBody())
            throw CadastrarUsuarioException(response.message())
        }

        return response.body()?.id
    }

}