package com.example.controlealimentar.model.enuns

enum class MessageLoading {
    TITULO("Aguarde um momento"),
    MENSAGEM("Carregando ...");

    var mensagem : String = ""

    constructor( mensagem: String){
        this.mensagem = mensagem
    }
}