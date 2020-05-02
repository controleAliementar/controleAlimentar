package com.example.controlealimentar.navigation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
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

    val args: ListaAlimentosRefeicaoFragmentArgs by navArgs()

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

        incluirAlimentoTextView.text = args.nomeRefeicao
        alterarHorarioRefeicaobutton.text = args.horarioRefeicao

        recycleViewListaAlimentoDetalhado.layoutManager = LinearLayoutManager(activity)
        recycleViewListaAlimentoDetalhado.adapter =
            AlimentoDetalhadoItemRecyclerViewAdapter(
                args.listAlimentos.asList(),
                this
            )

        incluirPorFotoButton
            .setOnClickListener( Navigation
                .createNavigateOnClickListener(R.id.action_listaAlimentosRefeicaoFragment_to_dicaFotoFragment))

        buscarAlimentoButton.setOnClickListener{
            val action = ListaAlimentosRefeicaoFragmentDirections
                .actionListaAlimentosRefeicaoFragmentToBuscarAlimentoFragment(null, args.idRefeicao, args.alimentoAvulso)
            view?.findNavController()?.navigate(action)
        }
    }

    override fun onAlimentoDetalhadoListFragmentInteraction(item: AlimentoDetalhado) {

    }


}
