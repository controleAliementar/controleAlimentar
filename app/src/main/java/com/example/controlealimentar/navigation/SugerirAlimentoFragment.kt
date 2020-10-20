package com.example.controlealimentar.navigation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.controlealimentar.R
import com.example.controlealimentar.databinding.FragmentSugerirAlimentoBinding

/**
 * A simple [Fragment] subclass.
 */
class SugerirAlimentoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSugerirAlimentoBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sugerir_alimento, container, false
        )

        val macroNutrienteNaoAlcancado : String = this.arguments?.get("macroNutrienteNaoAlcancado").toString()
        val alimentoSugerido : String = this.arguments?.get("alimentoSugerido").toString()

        val metaNaoAtingidaText = resources.getString(R.string.meta_nao_atingida_feedback)
        val sugestaoAlimentoText = resources.getString(R.string.sugesta_alimento_feedback)

        binding.metaNaoAtingidaText.text = String.format(metaNaoAtingidaText, "<b>$macroNutrienteNaoAlcancado</b>").parseAsHtml()
        binding.sugestaoText.text = String.format(sugestaoAlimentoText, "<b>$alimentoSugerido</b>").parseAsHtml()

        binding.okButton.setOnClickListener {
            findNavController().navigate(R.id.action_sugerirAlimentoFragment_to_homeFragment)
        }

        return binding.root
    }


}
