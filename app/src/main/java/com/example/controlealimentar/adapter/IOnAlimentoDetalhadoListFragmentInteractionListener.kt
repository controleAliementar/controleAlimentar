package com.example.controlealimentar.adapter

import com.example.controlealimentar.model.AlimentoDetalhado

interface IOnAlimentoDetalhadoListFragmentInteractionListener {

    fun onAlimentoDetalhadoListFragmentInteraction(item: AlimentoDetalhado)
    fun onAlimentoEditDetalhadoListFragmentInteraction(item: AlimentoDetalhado)
    fun onAlimentoDeleteDetalhadoListFragmentInteraction(item: AlimentoDetalhado)
}