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
import com.example.controlealimentar.exception.BuscarMetaDiariasException
import com.example.controlealimentar.model.Refeicao
import com.example.controlealimentar.model.enuns.MessageLoading
import com.example.controlealimentar.model.enuns.Refeicoes
import com.example.controlealimentar.model.enuns.SharedIds
import com.example.controlealimentar.service.RefeicaoService
import com.example.controlealimentar.util.CustomProgressBar
import com.example.controlealimentar.util.SharedPreference
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(),
    IOnRefeicaoListFragmentInteractionListener {

    private val refeicaoService : RefeicaoService =
        RefeicaoService()
    val progressBar = CustomProgressBar()

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

        val sharedPreference = SharedPreference(context)
        val processoId = sharedPreference.getValueString(SharedIds.ID_USUARIO.name)

        if (processoId.isNullOrBlank()){
            throw BuscarMetaDiariasException("ProcessoId não encontrado no sharedPreference")
        }

        progressBar.show(this.requireContext(), MessageLoading.MENSAGEM_GARREGANDO.mensagem)
        refeicaoService.buscarListaRefeicoesConsolidado(processoId,
            {
                recycleView.layoutManager = LinearLayoutManager(activity)
                recycleView.adapter =
                    RefeicaoItemRecyclerViewAdapter(
                        it,
                        this
                    )
                progressBar.dialog.dismiss()
            },
            {
                progressBar.dialog.dismiss()
                val action = HomeFragmentDirections
                    .actionHomeFragmentToErroGenericoFragment()
                view?.findNavController()?.navigate(action)
            })

    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("HH:mm")
        return format.format(date)
    }

    override fun onRefeicaoListFragmentInteraction(item: Refeicao) {

        when(item.id) {
            Refeicoes.CAFE_MANHA.id -> {
                defineProximaTela(item)
            }
            Refeicoes.LANCHE_MANHA.id -> {
                defineProximaTela(item)
            }
            Refeicoes.ALMOCO.id -> {
                defineProximaTela(item)
            }
            Refeicoes.LANCHE_TARDE.id -> {
                defineProximaTela(item)
            }
            Refeicoes.JANTA.id -> {
                defineProximaTela(item)
            }
            Refeicoes.CHA_NOITE.id -> {
                defineProximaTela(item)
            }
            else -> {
                val action = HomeFragmentDirections
                    .actionHomeFragmentToIncluirAlimentoFragment(item.id, item.horario, item.nome, true)
                view?.findNavController()?.navigate(action)
            }
        }
    }

    private fun defineProximaTela(item: Refeicao) {

        val sharedPreference = SharedPreference(context)
        val processoId = sharedPreference.getValueString(SharedIds.ID_USUARIO.name)

        if (processoId.isNullOrBlank()){
            throw BuscarMetaDiariasException("ProcessoId não encontrado no sharedPreference")
        }

        progressBar.show(this.requireContext(), MessageLoading.MENSAGEM_GARREGANDO.mensagem)
        refeicaoService.buscarRefeicaoAlimentos(processoId, item.id,
            {
                if (it.isNullOrEmpty()){
                    progressBar.dialog.dismiss()
                    val action = HomeFragmentDirections
                        .actionHomeFragmentToIncluirAlimentoFragment(item.id, item.horario, item.nome)
                    view?.findNavController()?.navigate(action)
                }else {
                    progressBar.dialog.dismiss()
                    val action = HomeFragmentDirections
                        .actionHomeFragmentToListaAlimentosRefeicaoFragment(it.toTypedArray(),
                            horarioRefeicao = item.horario,
                            idRefeicao = item.id,
                            nomeRefeicao = item.nome)
                    view?.findNavController()?.navigate(action)
                }
            },
            {
                progressBar.dialog.dismiss()
                val action = HomeFragmentDirections
                    .actionHomeFragmentToErroGenericoFragment()
                view?.findNavController()?.navigate(action)
            })
    }

}
