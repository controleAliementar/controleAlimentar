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
import com.example.controlealimentar.R
import com.example.controlealimentar.databinding.FragmentIncluirAlimentoBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class IncluirAlimentoFragment : Fragment() {

    val args: IncluirAlimentoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentIncluirAlimentoBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_incluir_alimento, container, false
        )

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val action =
                        IncluirAlimentoFragmentDirections
                            .actionIncluirAlimentoFragmentToHomeFragment()
                    view?.findNavController()?.navigate(action)
                }
            })

        binding.incluirAlimentoTextView.text = args.nomeRefeicao

        if (args.idRefeicao == "6ab66802-e7e5-4fb9-ba9a-6e85f44771a8"){
            binding.iconHorarioRefeicaoIncluir.visibility = View.GONE
            binding.horarioRefeicaoIncluir.visibility = View.GONE
        } else {
            binding.horarioRefeicaoIncluir.text = convertLongToTime(args.horarioRefeicao)
        }

        binding.incluirPorFotoButton.setOnClickListener{
            incluirAlimento()
        }
        binding.incluirAlimentoText.setOnClickListener {
            incluirAlimento()
        }


        binding.buscarAlimentoButton.setOnClickListener{
            buscarAlimento()
        }
        binding.buscarAlimentoTextView.setOnClickListener {
            buscarAlimento()
        }

        binding.iconHorarioRefeicaoIncluir.setOnClickListener{
            val action = IncluirAlimentoFragmentDirections
                .actionIncluirAlimentoFragmentToEditarHorarioRefeicaoFragment(args.horarioRefeicao,
                    args.idRefeicao, args.nomeRefeicao, args.alimentoAvulso)
            view?.findNavController()?.navigate(action)
        }

        binding.horarioRefeicaoIncluir.setOnClickListener{
            val action = IncluirAlimentoFragmentDirections
                .actionIncluirAlimentoFragmentToEditarHorarioRefeicaoFragment(args.horarioRefeicao,
                    args.idRefeicao, args.nomeRefeicao, args.alimentoAvulso)
            view?.findNavController()?.navigate(action)
        }

        return binding.root
    }

    private fun buscarAlimento() {
        val action = IncluirAlimentoFragmentDirections
            .actionIncluirAlimentoFragmentToBuscarAlimentoFragment(
                null, args.idRefeicao, args.alimentoAvulso, args.horarioRefeicao, args.nomeRefeicao
            )
        view?.findNavController()?.navigate(action)
    }

    private fun incluirAlimento() {
        val action = IncluirAlimentoFragmentDirections
            .actionIncluirAlimentoFragmentToCadastrarDadosOcrFragment(
                args.idRefeicao,
                args.nomeRefeicao, args.alimentoAvulso, args.horarioRefeicao
            )
        view?.findNavController()?.navigate(action)
    }

    private fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("HH:mm")
        return format.format(date)
    }


}
