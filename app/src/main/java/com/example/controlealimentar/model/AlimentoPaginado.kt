package com.example.controlealimentar.model


data class AlimentoPaginado(
    val listAlimentos: ArrayList<Alimento>,
    val ehUltimaPagina: Boolean
)
