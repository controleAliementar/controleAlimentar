package com.example.controlealimentar.util

import android.widget.EditText

class ValidacaoFormatoMetas {

    fun validar(ediTxt: EditText, text : String){
        // Colocar ponto após digitação do quarto numero
        if (!text.contains('.') && text.replace("g", "").length > 4){
            val ultimoDigito = text.substring(4)
            val textFormatado = text.substring(0, 4) + "." + ultimoDigito
            ediTxt.setText(textFormatado)
            ediTxt.setSelection(textFormatado.length)
        }

        // Não deixar começar com ponto
        if (text.startsWith('.')){
            val replace = text.replace(".", "")
            ediTxt.setText(replace)
            ediTxt.setSelection(replace.length)
        }

        // Não deixar ter mais de cinco casas o numero
        val textSemPonto = text.replace(".", "")
        val textSemMascaraEPonto = textSemPonto.replace("g", "")
        if (textSemMascaraEPonto.length > 5){
            val substring = text.substring(0, text.lastIndex)
            ediTxt.setText(substring)
            ediTxt.setSelection(substring.length)
        }

        // Não deixar terminar com ponto
        val textSemMascara = text.replace("g", "")
        if (textSemMascara.length == 4 && text.endsWith(".")){
            val replace = text.replace(".", "")
            ediTxt.setText(replace)
            ediTxt.setSelection(replace.length)
        }


        // Não deixar ter mais de um ponto
        val quantidadePontos = text.count { string -> string.equals('.') }
        if (quantidadePontos > 1){
            val replace = text.replace(".", "")
            ediTxt.setText(replace)
            ediTxt.setSelection(replace.length)
        }


        // Não deixar ter mais de duas casas depois do ponto
        val indexPonto = text.indexOf('.')
        val ultimoIndex = text.lastIndex

        if (indexPonto != -1 && (ultimoIndex - indexPonto >= 2)){
            val substring = text.substring(0, text.lastIndex)
            ediTxt.setText(substring)
            ediTxt.setSelection(substring.length)
        }
    }
}