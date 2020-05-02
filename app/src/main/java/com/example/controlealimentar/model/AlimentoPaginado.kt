package com.example.controlealimentar.model


data class AlimentoPaginado(
    val listAlimentos: ArrayList<Alimento> = arrayListOf(),
    val ehUltimaPagina: Boolean = false
)
