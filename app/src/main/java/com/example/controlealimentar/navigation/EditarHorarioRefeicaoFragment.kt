package com.example.controlealimentar.navigation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.controlealimentar.R
import com.example.controlealimentar.databinding.FragmentEditarHorarioRefeicaoBinding

/**
 * A simple [Fragment] subclass.
 */
class EditarHorarioRefeicaoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentEditarHorarioRefeicaoBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_editar_horario_refeicao, container, false
        )

        binding.okButton.setOnClickListener(Navigation
            .createNavigateOnClickListener
                (R.id.action_horarioRefeicaoFragment_to_incluirAlimentoFragment))

        return binding.root
    }


}
