package com.example.controlealimentar.navigation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
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
import com.example.controlealimentar.exception.BuscarMetaDiariasException
import com.example.controlealimentar.model.AlimentoDetalhado
import com.example.controlealimentar.model.enuns.MessageLoading
import com.example.controlealimentar.model.enuns.SharedIds
import com.example.controlealimentar.service.AlimentoService
import com.example.controlealimentar.service.RefeicaoService
import com.example.controlealimentar.util.CustomProgressBar
import com.example.controlealimentar.util.PopUps
import com.example.controlealimentar.util.SharedPreference
import kotlinx.android.synthetic.main.fragment_lista_alimentos_refeicao.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ListaAlimentosRefeicaoFragment : Fragment(),
    IOnAlimentoDetalhadoListFragmentInteractionListener{

    val args: ListaAlimentosRefeicaoFragmentArgs by navArgs()
    private val alimentoService : AlimentoService = AlimentoService()
    private val refeicaoService : RefeicaoService = RefeicaoService()
    val progressBar = CustomProgressBar()
    val popUp = PopUps()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentListaAlimentosRefeicaoBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_lista_alimentos_refeicao, container, false
        )

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val action =
                        ListaAlimentosRefeicaoFragmentDirections
                            .actionListaAlimentosRefeicaoFragmentToHomeFragment()
                    view?.findNavController()?.navigate(action)
                }
            })

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val sharedPreference = SharedPreference(context)
        val processoId = sharedPreference.getValueString(SharedIds.ID_USUARIO.name)

        if (processoId.isNullOrBlank()){
            throw BuscarMetaDiariasException("ProcessoId não encontrado no sharedPreference")
        }

        if (args.listAlimentos.isNullOrEmpty()){
            progressBar.show(this.requireContext(), MessageLoading.MENSAGEM_GARREGANDO.mensagem)
            refeicaoService.buscarRefeicaoAlimentos(processoId, args.idRefeicao,
                {
                    if (it.isNullOrEmpty()){
                        progressBar.dialog.dismiss()
                        val action = ListaAlimentosRefeicaoFragmentDirections
                            .actionListaAlimentosRefeicaoFragmentToIncluirAlimentoFragment(args.idRefeicao,
                                args.horarioRefeicao, args.nomeRefeicao, args.alimentoAvulso)
                        view?.findNavController()?.navigate(action)
                    }else {
                        recycleViewListaAlimentoDetalhado.layoutManager = LinearLayoutManager(activity)
                        recycleViewListaAlimentoDetalhado.adapter =
                            AlimentoDetalhadoItemRecyclerViewAdapter(
                                it,
                                args.idRefeicao,
                                this
                            )
                        progressBar.dialog.dismiss()
                    }
                },
                {
                    progressBar.dialog.dismiss()
                    val action = HomeFragmentDirections
                        .actionHomeFragmentToErroGenericoFragment()
                    view?.findNavController()?.navigate(action)
                })
        }else {
            recycleViewListaAlimentoDetalhado.layoutManager = LinearLayoutManager(activity)
            recycleViewListaAlimentoDetalhado.adapter =
                AlimentoDetalhadoItemRecyclerViewAdapter(
                    args.listAlimentos!!.toList(),
                    args.idRefeicao,
                    this
                )
        }

        incluirAlimentoTextView.text = args.nomeRefeicao

        if (args.idRefeicao == "6ab66802-e7e5-4fb9-ba9a-6e85f44771a8"){
            alterarHorarioRefeicaobutton.visibility = View.GONE
        } else {
            alterarHorarioRefeicaobutton.text = convertLongToTime(args.horarioRefeicao)
        }

        incluirPorFotoButton
            .setOnClickListener( Navigation
                .createNavigateOnClickListener(R.id.action_listaAlimentosRefeicaoFragment_to_dicaFotoFragment))

        buscarAlimentoButton.setOnClickListener{
            val action = ListaAlimentosRefeicaoFragmentDirections
                .actionListaAlimentosRefeicaoFragmentToBuscarAlimentoFragment(null,
                    args.idRefeicao, args.alimentoAvulso, args.horarioRefeicao, args.nomeRefeicao)
            view?.findNavController()?.navigate(action)
        }

        alterarHorarioRefeicaobutton.setOnClickListener {
            val action = ListaAlimentosRefeicaoFragmentDirections
                .actionListaAlimentosRefeicaoFragmentToEditarHorarioRefeicaoFragment(
                    args.horarioRefeicao, args.idRefeicao, args.nomeRefeicao, args.alimentoAvulso,
                    args.listAlimentos)
            view?.findNavController()?.navigate(action)
        }

    }

    override fun onAlimentoDetalhadoListFragmentInteraction(item: AlimentoDetalhado) {
        val sharedPreference = SharedPreference(context)
        val processoId = sharedPreference.getValueString(SharedIds.ID_USUARIO.name)

        if (processoId.isNullOrBlank()){
            throw BuscarMetaDiariasException("ProcessoId não encontrado no sharedPreference")
        }

        progressBar.show(this.requireContext(), MessageLoading.MENSAGEM_SALVANDO.mensagem)
        alimentoService.consumirAlimento(processoId, item.idRegistro, !item.alimentoIngerido,
            {
                progressBar.dialog.dismiss()
            },
            {
                progressBar.dialog.dismiss()
                val action = ListaAlimentosRefeicaoFragmentDirections
                    .actionListaAlimentosRefeicaoFragmentToErroGenericoFragment()
                view?.findNavController()?.navigate(action)
            })
    }

    override fun onAlimentoEditDetalhadoListFragmentInteraction(item: AlimentoDetalhado) {
        val action = ListaAlimentosRefeicaoFragmentDirections
            .actionListaAlimentosRefeicaoFragmentToEditarAlimentoRefeicao()
        view?.findNavController()?.navigate(action)
    }

    override fun onAlimentoDeleteDetalhadoListFragmentInteraction(item: AlimentoDetalhado) {
        Toast.makeText(requireContext(), "Excluindo", Toast.LENGTH_LONG).show()
        popUp.show(requireContext(), "Num pode menino")
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("HH:mm")
        return format.format(date)
    }


}
