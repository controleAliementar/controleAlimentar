package com.example.controlealimentar.navigation


import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.core.text.parseAsHtml
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.controlealimentar.R
import com.example.controlealimentar.extensions.navigateSafe
import kotlinx.android.synthetic.main.fragment_sugerir_alimento.view.*

/**
 * A simple [Fragment] subclass.
 */
class SugerirAlimentoFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view: View = requireActivity().getLayoutInflater()
            .inflate(R.layout.fragment_sugerir_alimento, null)

        val alert = AlertDialog.Builder(activity)
        alert.setView(view)

        val macroNutrienteNaoAlcancado : String = this.arguments?.get("macroNutrienteNaoAlcancado").toString()
        val alimentoSugerido : String = this.arguments?.get("alimentoSugerido").toString()

        val metaNaoAtingidaText = resources.getString(R.string.meta_nao_atingida_feedback)
        val sugestaoAlimentoText = resources.getString(R.string.sugesta_alimento_feedback)

        view.metaNaoAtingidaText.text = String.format(metaNaoAtingidaText, "<b>$macroNutrienteNaoAlcancado</b>").parseAsHtml()
        view.sugestaoText.text = String.format(sugestaoAlimentoText, "<b>$alimentoSugerido</b>").parseAsHtml()

        view.okButton.setOnClickListener {
            dismiss()
            findNavController().navigateSafe(R.id.action_sugerirAlimentoFragment_to_homeFragment)
        }

        return alert.create()
    }


}
