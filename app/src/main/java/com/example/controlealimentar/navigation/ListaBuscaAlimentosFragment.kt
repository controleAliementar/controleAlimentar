package com.example.controlealimentar.navigation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.controlealimentar.R
import com.example.controlealimentar.adapter.IOnAlimentoListFragmentInteractionListener
import com.example.controlealimentar.adapter.impl.AlimentoItemRecyclerViewAdapter
import com.example.controlealimentar.databinding.FragmentListaBuscaAlimentosBinding
import com.example.controlealimentar.model.Alimento
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * A simple [Fragment] subclass.
 */
class ListaBuscaAlimentosFragment : Fragment(),
    IOnAlimentoListFragmentInteractionListener {

    val args: ListaBuscaAlimentosFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentListaBuscaAlimentosBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_lista_busca_alimentos, container, false
        )

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recycleView.layoutManager = LinearLayoutManager(activity)
        recycleView.adapter =
            AlimentoItemRecyclerViewAdapter(
                args.listAlimentos,
                this
            )

    }

    override fun onAlimentoListFragmentInteraction(item: Alimento) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
