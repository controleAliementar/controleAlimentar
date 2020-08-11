package com.example.controlealimentar.navigation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.controlealimentar.R
import com.example.controlealimentar.adapter.IOnHistoricoMetaListFragmentInteractionListener
import com.example.controlealimentar.adapter.impl.HistoricoMetaItemRecyclerViewAdapter
import com.example.controlealimentar.databinding.FragmentListaMetasBinding
import com.example.controlealimentar.model.MetaDiarias

/**
 * A simple [Fragment] subclass.
 */
class ListaMetasFragment : Fragment(),
    IOnHistoricoMetaListFragmentInteractionListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentListaMetasBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_lista_metas, container, false
        )

        val metaDiarias1 = MetaDiarias()
        val metaDiarias2 = MetaDiarias()
        val metaDiarias3 = MetaDiarias()
        val metaDiarias4 = MetaDiarias()
        val metaDiarias5 = MetaDiarias()
        val metaDiarias6 = MetaDiarias()

        val listOf = listOf(
            metaDiarias1,
            metaDiarias2,
            metaDiarias3,
            metaDiarias4,
            metaDiarias5,
            metaDiarias6
        )

        binding.recycleViewListMetas.layoutManager = LinearLayoutManager(activity)
        binding.recycleViewListMetas.adapter = HistoricoMetaItemRecyclerViewAdapter(
            listOf,
            this
        )

        return binding.root
    }

    override fun onHistoricoMetaListFragmentInteraction(item: MetaDiarias) {

    }

}
