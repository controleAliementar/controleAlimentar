package com.example.controlealimentar.util

import android.app.ProgressDialog
import android.content.Context
import com.example.controlealimentar.model.enuns.MessageLoading

class Loading(var context: Context ?){

    val progress = ProgressDialog(context)

    fun criar() {
        progress.setTitle(MessageLoading.TITULO.mensagem)
        progress.setMessage(MessageLoading.MENSAGEM.mensagem)
        progress.setCancelable(false)
        progress.show()
    }

    fun remover() {
        progress.dismiss()
    }
}