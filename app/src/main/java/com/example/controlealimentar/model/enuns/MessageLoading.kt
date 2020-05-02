package com.example.controlealimentar.model.enuns

enum class MessageLoading {
    MENSAGEM_GARREGANDO("Carregando ..."),
    MENSAGEM_SALVANDO("Salvando ..."),
    MENSAGEM_BUSCANDO("Buscando ...");

    var mensagem : String = ""

    constructor( mensagem: String){
        this.mensagem = mensagem
    }
}