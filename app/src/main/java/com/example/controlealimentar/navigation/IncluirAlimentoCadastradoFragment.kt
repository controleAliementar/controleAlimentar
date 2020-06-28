package com.example.controlealimentar.navigation


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.controlealimentar.R
import com.example.controlealimentar.databinding.FragmentIncluirAlimentoCadastradoBinding
import com.example.controlealimentar.util.ValidacaoFormatoMetas
import kotlinx.android.synthetic.main.fragment_buscar_alimento.caloriaValue
import kotlinx.android.synthetic.main.fragment_buscar_alimento.carboidratosValue
import kotlinx.android.synthetic.main.fragment_buscar_alimento.gorduraValue
import kotlinx.android.synthetic.main.fragment_buscar_alimento.proteinasValue
import kotlinx.android.synthetic.main.fragment_incluir_alimento_cadastrado.*
import java.text.DecimalFormat

/**
 * A simple [Fragment] subclass.
 */
class IncluirAlimentoCadastradoFragment : Fragment() {

    val args: IncluirAlimentoCadastradoFragmentArgs by navArgs()
    val metas = ValidacaoFormatoMetas()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentIncluirAlimentoCadastradoBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_incluir_alimento_cadastrado, container, false
        )

        val alimentoTabelaNutricional = args.alimentoTabelaNutricional
        val decimal = DecimalFormat("#,###.0")

        binding.nomeAlimentoTextView.text = alimentoTabelaNutricional.nomeAlimento
        binding.porcaoText.text = alimentoTabelaNutricional.unidadePorcao
        binding.caloriaValue.text = decimal.format(alimentoTabelaNutricional.calorias)
        binding.carboidratosValue.text = decimal.format(alimentoTabelaNutricional.carboidratos)
        binding.proteinasValue.text = decimal.format(alimentoTabelaNutricional.proteinas)
        binding.gorduraValue.text = decimal.format(alimentoTabelaNutricional.gorduras)

        binding.salvarAlimentoTabelaNutricionalButton.isEnabled = false

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        valorPorcaoValueText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                preencherCamposMacronutrientes()
            }

            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                metas.validar(valorPorcaoValueText, valorPorcaoValueText.text.toString())
                salvarAlimentoTabelaNutricionalButton.isEnabled = !text.isNullOrBlank()
            }
        })

    }

    private fun preencherCamposMacronutrientes(){
        if (!valorPorcaoValueText.text.toString().isBlank()){

            val porcaoDigitada = java.lang.Double.parseDouble(valorPorcaoValueText.text.toString())

            caloriaValue.text = calcularQuantidadeGramas(porcaoDigitada, args.alimentoTabelaNutricional.calorias)
            carboidratosValue.text = calcularQuantidadeGramas(porcaoDigitada, args.alimentoTabelaNutricional.carboidratos)
            proteinasValue.text = calcularQuantidadeGramas(porcaoDigitada, args.alimentoTabelaNutricional.proteinas)
            gorduraValue.text = calcularQuantidadeGramas(porcaoDigitada, args.alimentoTabelaNutricional.gorduras)
        }
    }

    private fun calcularQuantidadeGramas(valorPorcaoInserido: Double, valorMacronutriente: Double) : String {
        val resultado = (valorMacronutriente / args.alimentoTabelaNutricional.porcao) * valorPorcaoInserido
        val decimal = DecimalFormat("#,###.0")
        return decimal.format(resultado)
    }

}
