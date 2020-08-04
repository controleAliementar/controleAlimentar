package com.example.controlealimentar.navigation


import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.controlealimentar.R
import com.example.controlealimentar.extensions.navigateSafe
import com.example.controlealimentar.model.FeedbackUsuario
import com.example.controlealimentar.model.enuns.SharedIds
import com.example.controlealimentar.service.UsuarioService
import com.example.controlealimentar.util.SharedPreference
import kotlinx.android.synthetic.main.fragment_exibir_feedback.view.*
import java.text.DecimalFormat

/**
 * A simple [Fragment] subclass.
 */
class ExibirFeedbackFragment : DialogFragment() {

    val usuarioService = UsuarioService()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view: View = requireActivity().getLayoutInflater()
            .inflate(R.layout.fragment_exibir_feedback, null)

        val sharedPreference = SharedPreference(context)
        val processoId = sharedPreference.getValueString(SharedIds.ID_USUARIO.name)

        usuarioService.buscarFeedbackUsuario(
            processoId!!,
            {

                preencheBarrasMetas(it, view)

                setGif(it, view)

            },{
                findNavController().navigateSafe(R.id.action_exibirFeedbackFragment2_to_homeFragment)
            })

        val alert = AlertDialog.Builder(activity)
        alert.setView(view)

        view.exit.setOnClickListener {

            usuarioService.atualizarFeedbackUsuario(processoId,
                {
                    findNavController().navigateSafe(R.id.action_exibirFeedbackFragment2_to_homeFragment)
                },
                {
                    findNavController().navigateSafe(R.id.action_exibirFeedbackFragment2_to_homeFragment)
                })

        }

        return alert.create()
    }

    private fun setGif(
        it: FeedbackUsuario,
        view: View
    ) {
        if (isMetaAtingida(it.caloriasMeta, it.caloriasConsumidas) &&
            isMetaAtingida(it.carboidratosMeta, it.carboidratosConsumidas) &&
            isMetaAtingida(it.proteinasMeta, it.proteinasConsumidas) &&
            isMetaAtingida(it.gordurasMeta, it.gordurasConsumidas)
        ) {

            view.emoji.setImageResource(R.drawable.success_gif)

        }
    }

    private fun preencheBarrasMetas(feedbackUsuario: FeedbackUsuario, view: View){

        view.caloriaProgressBar.progress = calculaPorcentagemMeta(feedbackUsuario.caloriasMeta, feedbackUsuario.caloriasConsumidas)
        view.caloriaProgressNumber.text = feedbackUsuario.caloriasConsumidas.toString().replace(".", ",")
        view.caloriaProgressNumberTotal.text = feedbackUsuario.caloriasMeta.toString().replace(".", ",")
        defineCorProgressBar(view.caloriaProgressBar)

        view.proteinaProgressBar.progress = calculaPorcentagemMeta(feedbackUsuario.proteinasMeta, feedbackUsuario.proteinasConsumidas)
        view.proteinaProgressNumber.text = feedbackUsuario.proteinasConsumidas.toString().replace(".", ",") + "g"
        view.proteinaProgressNumberTotal.text = feedbackUsuario.proteinasMeta.toString().replace(".", ",") + "g"
        defineCorProgressBar(view.proteinaProgressBar)

        view.carboidratoProgressBar.progress = calculaPorcentagemMeta(feedbackUsuario.carboidratosMeta, feedbackUsuario.carboidratosConsumidas)
        view.carboidratoProgressNumber.text = feedbackUsuario.carboidratosConsumidas.toString().replace(".", ",") + "g"
        view.carboidratoProgressNumberTotal.text = feedbackUsuario.carboidratosMeta.toString().replace(".", ",") + "g"
        defineCorProgressBar(view.carboidratoProgressBar)

        view.gorduraProgressBar.progress = calculaPorcentagemMeta(feedbackUsuario.gordurasMeta, feedbackUsuario.gordurasConsumidas)
        view.gorduraProgressNumber.text = feedbackUsuario.gordurasConsumidas.toString().replace(".", ",") + "g"
        view.gorduraProgressNumberTotal.text = feedbackUsuario.gordurasMeta.toString().replace(".", ",") + "g"
        defineCorProgressBar(view.gorduraProgressBar)

    }

    private fun calculaPorcentagemMeta(meta: Double, metaConsumida: Double): Int{
        val total = (metaConsumida * 100)/ meta
        val decimal = DecimalFormat("#")
        return decimal.format(total).toInt()
    }

    private fun defineCorProgressBar(progressBar: ProgressBar){
        val porcentagem = progressBar.progress

        when {
            porcentagem >= 75 -> progressBar.progressDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.progress_limit75)
            porcentagem >= 50 -> progressBar.progressDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.progress_limit50)
            porcentagem >= 25 -> progressBar.progressDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.progress_limit25)
            else -> progressBar.progressDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.progress_limit)
        }

    }

    private fun isMetaAtingida(meta: Double, metaConsumida: Double): Boolean{
        val total = (metaConsumida * 100)/ meta
        val decimal = DecimalFormat("#")
        val porcentagem = decimal.format(total).toInt()
        return porcentagem in 90..110
    }

}
