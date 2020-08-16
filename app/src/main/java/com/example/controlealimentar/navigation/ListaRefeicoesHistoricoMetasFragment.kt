package com.example.controlealimentar.navigation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.controlealimentar.R
import com.example.controlealimentar.adapter.IOnHistoricoListRefeicoesFragmentInteractionListener
import com.example.controlealimentar.adapter.impl.HistoricoRefeicaoItemRecyclerViewAdapter
import com.example.controlealimentar.databinding.FragmentListaRefeicoesHistoricoMetasBinding
import com.example.controlealimentar.model.Refeicao

/**
 * A simple [Fragment] subclass.
 */
class ListaRefeicoesHistoricoMetasFragment : Fragment(),
        IOnHistoricoListRefeicoesFragmentInteractionListener{
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentListaRefeicoesHistoricoMetasBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_lista_refeicoes_historico_metas, container, false
        )

        val metaDiarias1 = Refeicao("1", "Café", 123456L, 1000.0, 500.0, 200.0, 1000.0)
        val metaDiarias2 = Refeicao("1", "Café", 123456L, 1000.0, 500.0, 200.0, 1000.0)
        val metaDiarias3 = Refeicao("1", "Café", 123456L, 1000.0, 500.0, 200.0, 1000.0)
        val metaDiarias4 = Refeicao("1", "Café", 123456L, 1000.0, 500.0, 200.0, 1000.0)
        val metaDiarias5 = Refeicao("1", "Café", 123456L, 1000.0, 500.0, 200.0, 1000.0)
        val metaDiarias6 = Refeicao("1", "Café", 123456L, 1000.0, 500.0, 200.0, 1000.0)

        val listOf = listOf(
            metaDiarias1,
            metaDiarias2,
            metaDiarias3,
            metaDiarias4,
            metaDiarias5,
            metaDiarias6
        )

        binding.recycleViewListRefeicoes.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleViewListRefeicoes.adapter = HistoricoRefeicaoItemRecyclerViewAdapter(
            listOf,
            this
        )

        return binding.root
    }

    override fun onHistoricoListRefeicoesFragmentInteraction(item: Refeicao) {

    }

}
