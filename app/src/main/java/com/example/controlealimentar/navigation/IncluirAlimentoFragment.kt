package com.example.controlealimentar.navigation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.controlealimentar.R
import com.example.controlealimentar.databinding.FragmentIncluirAlimentoBinding

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

        binding.incluirAlimentoTextView.text = args.tipoRefeicao
        binding.alterarHorarioRefeicaobutton.text = args.horarioRefeicao

        binding.incluirPorFotoButton
            .setOnClickListener( Navigation
                .createNavigateOnClickListener(R.id.action_incluirAlimentoFragment_to_dicaFotoFragment))

        return binding.root
    }


}
