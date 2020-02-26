package com.example.controlealimentar.navigation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.controlealimentar.R
import com.example.controlealimentar.adapter.IOnListFragmentInteractionListener
import com.example.controlealimentar.adapter.RefeicaoItemRecyclerViewAdapter
import com.example.controlealimentar.databinding.FragmentHomeBinding
import com.example.controlealimentar.model.Refeicao
import com.example.controlealimentar.model.enuns.Refeicoes
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(),
    IOnListFragmentInteractionListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentHomeBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false
        )

        binding.editarMetasbutton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_editarMetasFragment))

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recycleView.layoutManager = LinearLayoutManager(activity)
        recycleView.adapter =
            RefeicaoItemRecyclerViewAdapter(fackData(), this)

    }

    private fun fackData() = listOf(
        Refeicao(nome = Refeicoes.ALIMENTOS_AVULSOS.nome, horario = "7:15", caloria = 0, carboidrato = 0, gordura = 0, proteina = 0),
        Refeicao(nome = Refeicoes.CAFE_MANHA.nome, horario = "7:15", caloria = 0, carboidrato = 0, gordura = 0, proteina = 0),
        Refeicao(nome = Refeicoes.LANCHE_MANHA.nome, horario = "9:15", caloria = 0, carboidrato = 0, gordura = 0, proteina = 0),
        Refeicao(nome = Refeicoes.ALMOCO.nome, horario = "12:15", caloria = 0, carboidrato = 0, gordura = 0, proteina = 0),
        Refeicao(nome = Refeicoes.LANCHE_TARDE.nome, horario = "16:15", caloria = 0, carboidrato = 0, gordura = 0, proteina = 0),
        Refeicao(nome = Refeicoes.JANTA.nome, horario = "20:15", caloria = 0, carboidrato = 0, gordura = 0, proteina = 0),
        Refeicao(nome = Refeicoes.CHA_NOITE.nome, horario = "22:15", caloria = 0, carboidrato = 0, gordura = 0, proteina = 0)
    )

    override fun onListFragmentInteraction(item: Refeicao) {

        when(item.nome) {
            Refeicoes.CAFE_MANHA.nome -> {
                val action = HomeFragmentDirections
                .actionHomeFragmentToIncluirAlimentoFragment(Refeicoes.CAFE_MANHA.nome)
                view?.findNavController()?.navigate(action)
            }
            Refeicoes.LANCHE_MANHA.nome -> {
                val action = HomeFragmentDirections
                    .actionHomeFragmentToIncluirAlimentoFragment(Refeicoes.LANCHE_MANHA.nome)
                view?.findNavController()?.navigate(action)
            }
            Refeicoes.ALMOCO.nome -> {
                val action = HomeFragmentDirections
                    .actionHomeFragmentToIncluirAlimentoFragment(Refeicoes.ALMOCO.nome)
                view?.findNavController()?.navigate(action)
            }
            Refeicoes.LANCHE_TARDE.nome -> {
                val action = HomeFragmentDirections
                    .actionHomeFragmentToIncluirAlimentoFragment(Refeicoes.LANCHE_TARDE.nome)
                view?.findNavController()?.navigate(action)
            }
            Refeicoes.JANTA.nome -> {
                val action = HomeFragmentDirections
                    .actionHomeFragmentToIncluirAlimentoFragment(Refeicoes.JANTA.nome)
                view?.findNavController()?.navigate(action)
            }
            Refeicoes.CHA_NOITE.nome -> {
                val action = HomeFragmentDirections
                    .actionHomeFragmentToIncluirAlimentoFragment(Refeicoes.CHA_NOITE.nome)
                view?.findNavController()?.navigate(action)
            }
            else -> {
                val action = HomeFragmentDirections
                    .actionHomeFragmentToIncluirAlimentoFragment(Refeicoes.ALIMENTOS_AVULSOS.nome)
                view?.findNavController()?.navigate(action)
            }
        }
    }

}
