package com.example.controlealimentar.navigation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.controlealimentar.R
import com.example.controlealimentar.databinding.FragmentExibirMetaDetalhadaBinding
import com.example.controlealimentar.model.MetaDiariasHistorico
import kotlinx.android.synthetic.main.fragment_exibir_feedback.view.*
import java.text.DecimalFormat

/**
 * A simple [Fragment] subclass.
 */
class ExibirMetaDetalhadaFragment : Fragment() {

    val args: ExibirMetaDetalhadaFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentExibirMetaDetalhadaBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_exibir_meta_detalhada, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preencheBarrasMetas(args.metaDiariaHistorico, requireView())
        setGif(args.metaDiariaHistorico, requireView())
    }

    private fun setGif(
        it: MetaDiariasHistorico,
        view: View
    ) {
        if (isMetaAtingida(it.calorias, it.caloriasConsumidas) &&
            isMetaAtingida(it.carboidratos, it.carboidratosConsumidas) &&
            isMetaAtingida(it.proteinas, it.proteinasConsumidas) &&
            isMetaAtingida(it.gorduras, it.gordurasConsumidas)
        ) {

            view.emoji.setImageResource(R.drawable.success_gif)

        }
    }

    private fun preencheBarrasMetas(feedbackUsuario: MetaDiariasHistorico, view: View){

        view.caloriaProgressBar.progress = calculaPorcentagemMeta(feedbackUsuario.calorias, feedbackUsuario.caloriasConsumidas)
        view.caloriaProgressNumber.text = feedbackUsuario.caloriasConsumidas.toString().replace(".", ",")
        view.caloriaProgressNumberTotal.text = feedbackUsuario.calorias.toString().replace(".", ",")
        defineCorProgressBar(view.caloriaProgressBar)

        view.proteinaProgressBar.progress = calculaPorcentagemMeta(feedbackUsuario.proteinas, feedbackUsuario.proteinasConsumidas)
        view.proteinaProgressNumber.text = feedbackUsuario.proteinasConsumidas.toString().replace(".", ",") + "g"
        view.proteinaProgressNumberTotal.text = feedbackUsuario.proteinas.toString().replace(".", ",") + "g"
        defineCorProgressBar(view.proteinaProgressBar)

        view.carboidratoProgressBar.progress = calculaPorcentagemMeta(feedbackUsuario.carboidratos, feedbackUsuario.carboidratosConsumidas)
        view.carboidratoProgressNumber.text = feedbackUsuario.carboidratosConsumidas.toString().replace(".", ",") + "g"
        view.carboidratoProgressNumberTotal.text = feedbackUsuario.carboidratos.toString().replace(".", ",") + "g"
        defineCorProgressBar(view.carboidratoProgressBar)

        view.gorduraProgressBar.progress = calculaPorcentagemMeta(feedbackUsuario.gorduras, feedbackUsuario.gordurasConsumidas)
        view.gorduraProgressNumber.text = feedbackUsuario.gordurasConsumidas.toString().replace(".", ",") + "g"
        view.gorduraProgressNumberTotal.text = feedbackUsuario.gorduras.toString().replace(".", ",") + "g"
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
