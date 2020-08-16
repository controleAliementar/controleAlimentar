package com.example.controlealimentar.adapter

import android.view.View
import com.example.controlealimentar.model.AlimentoDetalhado

interface IOnAlimentoDetalhadoListFragmentInteractionListener {

    fun onAlimentoDetalhadoListFragmentInteraction(item: AlimentoDetalhado, view: View)
    fun onAlimentoEditDetalhadoListFragmentInteraction(item: AlimentoDetalhado)
    fun onAlimentoDeleteDetalhadoListFragmentInteraction(item: AlimentoDetalhado)
}