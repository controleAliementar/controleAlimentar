package com.example.controlealimentar.service

import android.app.ProgressDialog
import android.content.Context
import com.example.controlealimentar.config.MessageLoading
import com.example.controlealimentar.config.RetrofitConfig
import com.example.controlealimentar.exception.CadastrarUsuarioException
import com.example.controlealimentar.gateway.data.UsuarioRequestGateway
import com.example.controlealimentar.model.Usuario

class UsuarioService {

    private val retrofitConfig: RetrofitConfig = RetrofitConfig()

    fun salvarUsuario(usuario: Usuario, context: Context?) : String? {

        val usuarioGateway = UsuarioRequestGateway(
            usuario.email,
            usuario.nome)

        val loading = criarLoading(context)

        val response = retrofitConfig.getUsuarioGateway()!!
            .salvarUsuario(usuarioGateway)
            .execute()

        if (!response!!.isSuccessful){
            print(response.errorBody())
            removerLoading(loading)
            throw CadastrarUsuarioException(response.message())
        }

        removerLoading(loading)
        return response.body()?.id
    }

    private fun criarLoading(context: Context?) : ProgressDialog{
        val progress = ProgressDialog(context)
        progress.setTitle(MessageLoading.TITULO.mensagem)
        progress.setMessage(MessageLoading.MENSAGEM.mensagem)
        progress.setCancelable(false)
        progress.show()
        return progress
    }

    private fun removerLoading(progressDoalog: ProgressDialog) {
        progressDoalog.dismiss()
    }

}