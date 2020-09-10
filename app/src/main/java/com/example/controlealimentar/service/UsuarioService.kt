package com.example.controlealimentar.service

import android.util.Log
import com.example.controlealimentar.config.RetrofitConfig
import com.example.controlealimentar.exception.AtualizarFeedbackUsuarioException
import com.example.controlealimentar.exception.BuscarFeedbackUsuarioException
import com.example.controlealimentar.exception.BuscarUsuarioPorEmailException
import com.example.controlealimentar.exception.CadastrarUsuarioException
import com.example.controlealimentar.gateway.data.FeedbackUsuarioResponseGateway
import com.example.controlealimentar.gateway.data.UsuarioRequestGateway
import com.example.controlealimentar.gateway.data.UsuarioResponseGateway
import com.example.controlealimentar.model.FeedbackUsuario
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
            usuario.nome,
            usuario.tokenFirebase)

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

    fun buscarFeedbackUsuario(processoId: String,
                              onSuccess : (FeedbackUsuario) -> Unit,
                              onError : (Exception) -> Unit) {

        val call = retrofitConfig.getUsuarioGateway()!!
            .buscarFeedbackUsuario(processoId)

        call.enqueue(object : Callback<FeedbackUsuarioResponseGateway> {
            override fun onResponse(call: Call<FeedbackUsuarioResponseGateway>,
                                    response: Response<FeedbackUsuarioResponseGateway>
            ) {
                if (!response.isSuccessful){
                    print(response.errorBody())
                    return onError(BuscarFeedbackUsuarioException(response.errorBody().toString()))
                }
                if (response.code() == 204){
                    onSuccess(FeedbackUsuario())
                }

                response.body()?.let {

                    val feedbackUsuario = FeedbackUsuario()
                    feedbackUsuario.caloriasConsumidas = it.caloriasConsumidas
                    feedbackUsuario.caloriasMeta = it.caloriasMeta
                    feedbackUsuario.carboidratosConsumidas = it.carboidratosConsumidas
                    feedbackUsuario.carboidratosMeta = it.carboidratosMeta
                    feedbackUsuario.gordurasConsumidas = it.gordurasConsumidas
                    feedbackUsuario.gordurasMeta = it.gordurasMeta
                    feedbackUsuario.proteinasConsumidas = it.proteinasConsumidas
                    feedbackUsuario.proteinasMeta = it.proteinasMeta
                    feedbackUsuario.status = it.status
                    feedbackUsuario.data = it.data

                    onSuccess(feedbackUsuario)
                }
            }

            override fun onFailure(call: Call<FeedbackUsuarioResponseGateway>, t: Throwable?) {
                Log.e("Deu ruim: ", t?.message)
                onError(BuscarFeedbackUsuarioException(t?.message))
            }
        })

    }

    fun atualizarFeedbackUsuario(processoId: String,
                              onSuccess : () -> Unit,
                              onError : (Exception) -> Unit) {

        val call = retrofitConfig.getUsuarioGateway()!!
            .atualizarFeedbackUsuario(processoId)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>,
                                    response: Response<Void>
            ) {
                if (!response.isSuccessful){
                    print(response.errorBody())
                    return onError(AtualizarFeedbackUsuarioException(response.errorBody().toString()))
                }

                onSuccess()
            }

            override fun onFailure(call: Call<Void>, t: Throwable?) {
                Log.e("Deu ruim: ", t?.message)
                onError(AtualizarFeedbackUsuarioException(t?.message))
            }
        })

    }

    fun buscarUsuarioPorEmail(email: String,
                              onSuccess : (Usuario) -> Unit,
                              onError : (Exception) -> Unit) {

        val call = retrofitConfig.getUsuarioGateway()!!
            .buscarUsuarioPorEmail(email)

        call.enqueue(object : Callback<UsuarioResponseGateway> {
            override fun onResponse(call: Call<UsuarioResponseGateway>,
                                    response: Response<UsuarioResponseGateway>
            ) {
                if (!response.isSuccessful){
                    print(response.errorBody())
                    return onError(BuscarUsuarioPorEmailException(response.errorBody().toString()))
                }
                if (response.code() == 204){
                    onSuccess(Usuario())
                }

                response.body()?.let {

                    val usuario = Usuario()
                    usuario.id = it.id
                    usuario.nome = it.nome
                    usuario.email = it.email

                    onSuccess(usuario)
                }
            }

            override fun onFailure(call: Call<UsuarioResponseGateway>, t: Throwable?) {
                Log.e("Deu ruim: ", t?.message)
                onError(BuscarUsuarioPorEmailException(t?.message))
            }
        })

    }

}