package com.example.controlealimentar.service

import android.util.Log
import com.example.controlealimentar.config.RetrofitConfig
import com.example.controlealimentar.exception.CadastrarUsuarioException
import com.example.controlealimentar.gateway.data.UsuarioRequestGateway
import com.example.controlealimentar.gateway.data.UsuarioResponseGateway
import com.example.controlealimentar.model.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsuarioService {

    private val retrofitConfig: RetrofitConfig = RetrofitConfig()

    fun salvarUsuario(usuario: Usuario,
                      onSuccess : (String) -> Unit,
                      onError : (Exception) -> Unit) {

        val usuarioGateway = UsuarioRequestGateway(
            usuario.email,
            usuario.nome)

        val call = retrofitConfig.getUsuarioGateway()!!
            .salvarUsuario(usuarioGateway)

        call.enqueue(object : Callback<UsuarioResponseGateway> {
            override fun onResponse(call: Call<UsuarioResponseGateway>,
                                    response: Response<UsuarioResponseGateway>
            ) {
                if (!response.isSuccessful){
                    print(response.errorBody())
                    return onError(CadastrarUsuarioException(response.errorBody().toString()))
                }

                response.body()?.let {
                    onSuccess(it.id)
                }
            }

            override fun onFailure(call: Call<UsuarioResponseGateway>, t: Throwable?) {
                Log.e("Deu ruim: ", t?.message)
                onError(CadastrarUsuarioException(t?.message))
            }
        })

    }

}