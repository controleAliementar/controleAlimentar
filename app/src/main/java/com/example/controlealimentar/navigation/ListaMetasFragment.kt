package com.example.controlealimentar.navigation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.controlealimentar.R
import com.example.controlealimentar.adapter.IOnHistoricoMetaListFragmentInteractionListener
import com.example.controlealimentar.adapter.impl.HistoricoMetaItemRecyclerViewAdapter
import com.example.controlealimentar.databinding.FragmentListaMetasBinding
import com.example.controlealimentar.model.MetaDiariasHistorico

/**
 * A simple [Fragment] subclass.
 */
class ListaMetasFragment : Fragment(),
    IOnHistoricoMetaListFragmentInteractionListener {

    val args: ListaMetasFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentListaMetasBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_lista_metas, container, false
        )

        binding.recycleViewListMetas.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleViewListMetas.adapter = HistoricoMetaItemRecyclerViewAdapter(
            args.listMetas,
            this
        )

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val action =
                        ListaMetasFragmentDirections
                            .actionListaMetasFragmentToHomeFragment()
                    view?.findNavController()?.navigate(action)
                }
            })

        binding.floatingActionButtonCalendar.setOnClickListener {
            val action =
                ListaMetasFragmentDirections
                    .actionListaMetasFragmentToBuscarHistoricoMetasFragment()
            view?.findNavController()?.navigate(action)
        }

        return binding.root
    }

    override fun onHistoricoMetaListFragmentInteraction(item: MetaDiariasHistorico) {
        val action =
            ListaMetasFragmentDirections.actionListaMetasFragmentToExibirMetaDetalhadaFragment(item)
        view?.findNavController()?.navigate(action)
    }

    override fun onHistoricoMetaDetalhadaIconeFragmentInteraction(item: MetaDiariasHistorico) {
        val action =
            ListaMetasFragmentDirections.actionListaMetasFragmentToExibirMetaDetalhadaFragment(item)
        view?.findNavController()?.navigate(action)
    }

}
