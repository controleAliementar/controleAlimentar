package com.example.controlealimentar.model.enuns

enum class Refeicoes {
    CAFE_MANHA("Café da manhã"),
    LANCHE_MANHA("Lanche da manhã"),
    ALMOCO("Almoço"),
    LANCHE_TARDE("Lanche da tarde"),
    JANTA("Janta"),
    CHA_NOITE("Chá da noite"),
    ALIMENTOS_AVULSOS("Alimentos avulsos");

    var nome: String

    constructor(nome: String){
        this.nome = nome
    }

}