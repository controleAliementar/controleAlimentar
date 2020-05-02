package com.example.controlealimentar.navigation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.controlealimentar.R
import com.example.controlealimentar.adapter.IOnAlimentoDetalhadoListFragmentInteractionListener
import com.example.controlealimentar.adapter.impl.AlimentoDetalhadoItemRecyclerViewAdapter
import com.example.controlealimentar.databinding.FragmentListaAlimentosRefeicaoBinding
import com.example.controlealimentar.model.AlimentoDetalhado
import kotlinx.android.synthetic.main.fragment_lista_alimentos_refeicao.*

/**
 * A simple [Fragment] subclass.
 */
class ListaAlimentosRefeicaoFragment : Fragment(),
    IOnAlimentoDetalhadoListFragmentInteractionListener{

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentListaAlimentosRefeicaoBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_lista_alimentos_refeicao, container, false
        )

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val alimentoDetalhadoList = ArrayList<AlimentoDetalhado>()
        alimentoDetalhadoList.add(AlimentoDetalhado(nomeAlimento = "Xuxu"))

        recycleViewListaAlimentoDetalhado.layoutManager = LinearLayoutManager(activity)
        recycleViewListaAlimentoDetalhado.adapter =
            AlimentoDetalhadoItemRecyclerViewAdapter(
                alimentoDetalhadoList,
                this
            )
    }

    override fun onAlimentoDetalhadoListFragmentInteraction(item: AlimentoDetalhado) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
