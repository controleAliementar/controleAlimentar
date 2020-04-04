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
import com.example.controlealimentar.adapter.IOnRefeicaoListFragmentInteractionListener
import com.example.controlealimentar.adapter.impl.RefeicaoItemRecyclerViewAdapter
import com.example.controlealimentar.databinding.FragmentHomeBinding
import com.example.controlealimentar.model.Refeicao
import com.example.controlealimentar.model.enuns.Refeicoes
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(),
    IOnRefeicaoListFragmentInteractionListener {

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
            RefeicaoItemRecyclerViewAdapter(
                fakeData(),
                this
            )

    }

    private fun fakeData() = listOf(
        Refeicao(nome = Refeicoes.ALIMENTOS_AVULSOS.nome, horario = convertLongToTime(1583027867412), caloria = 0, carboidrato = 0, gordura = 0, proteina = 0),
        Refeicao(nome = Refeicoes.CAFE_MANHA.nome, horario = convertLongToTime(1583027867412), caloria = 0, carboidrato = 0, gordura = 0, proteina = 0),
        Refeicao(nome = Refeicoes.LANCHE_MANHA.nome, horario = convertLongToTime(1583027867412), caloria = 0, carboidrato = 0, gordura = 0, proteina = 0),
        Refeicao(nome = Refeicoes.ALMOCO.nome, horario = convertLongToTime(1583027867412), caloria = 0, carboidrato = 0, gordura = 0, proteina = 0),
        Refeicao(nome = Refeicoes.LANCHE_TARDE.nome, horario = convertLongToTime(1583027867412), caloria = 0, carboidrato = 0, gordura = 0, proteina = 0),
        Refeicao(nome = Refeicoes.JANTA.nome, horario =convertLongToTime(1583027867412), caloria = 0, carboidrato = 0, gordura = 0, proteina = 0),
        Refeicao(nome = Refeicoes.CHA_NOITE.nome, horario = convertLongToTime(1583027867412), caloria = 0, carboidrato = 0, gordura = 0, proteina = 0)
    )

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("HH:mm")
        return format.format(date)
    }

    override fun onRefeicaoListFragmentInteraction(item: Refeicao) {

        when(item.nome) {
            Refeicoes.CAFE_MANHA.nome -> {
                val action = HomeFragmentDirections
                .actionHomeFragmentToIncluirAlimentoFragment(Refeicoes.CAFE_MANHA.nome, item.horario)
                view?.findNavController()?.navigate(action)
            }
            Refeicoes.LANCHE_MANHA.nome -> {
                val action = HomeFragmentDirections
                    .actionHomeFragmentToIncluirAlimentoFragment(Refeicoes.LANCHE_MANHA.nome, item.horario)
                view?.findNavController()?.navigate(action)
            }
            Refeicoes.ALMOCO.nome -> {
                val action = HomeFragmentDirections
                    .actionHomeFragmentToIncluirAlimentoFragment(Refeicoes.ALMOCO.nome, item.horario)
                view?.findNavController()?.navigate(action)
            }
            Refeicoes.LANCHE_TARDE.nome -> {
                val action = HomeFragmentDirections
                    .actionHomeFragmentToIncluirAlimentoFragment(Refeicoes.LANCHE_TARDE.nome, item.horario)
                view?.findNavController()?.navigate(action)
            }
            Refeicoes.JANTA.nome -> {
                val action = HomeFragmentDirections
                    .actionHomeFragmentToIncluirAlimentoFragment(Refeicoes.JANTA.nome, item.horario)
                view?.findNavController()?.navigate(action)
            }
            Refeicoes.CHA_NOITE.nome -> {
                val action = HomeFragmentDirections
                    .actionHomeFragmentToIncluirAlimentoFragment(Refeicoes.CHA_NOITE.nome, item.horario)
                view?.findNavController()?.navigate(action)
            }
            else -> {
                val action = HomeFragmentDirections
                    .actionHomeFragmentToIncluirAlimentoFragment(Refeicoes.ALIMENTOS_AVULSOS.nome, item.horario)
                view?.findNavController()?.navigate(action)
            }
        }
    }

}
