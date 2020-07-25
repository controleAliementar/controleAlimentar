package com.example.controlealimentar.navigation


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.controlealimentar.R
import com.example.controlealimentar.databinding.FragmentIncluirAlimentoCadastradoBinding
import com.example.controlealimentar.exception.SalvarAlimentoUsuarioException
import com.example.controlealimentar.model.enuns.MessageLoading
import com.example.controlealimentar.model.enuns.SharedIds
import com.example.controlealimentar.service.AlimentoService
import com.example.controlealimentar.util.CustomProgressBar
import com.example.controlealimentar.util.SharedPreference
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
    val alimentoService = AlimentoService()
    val progressBar = CustomProgressBar()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentIncluirAlimentoCadastradoBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_incluir_alimento_cadastrado, container, false
        )

        val alimentoTabelaNutricional = args.alimentoUsuario

        binding.nomeAlimentoTextView.text = alimentoTabelaNutricional.nomeAlimento
        binding.porcaoText.text = alimentoTabelaNutricional.unidadePorcao

        binding.salvarAlimentoTabelaNutricionalButton.isEnabled = false

        binding.salvarAlimentoTabelaNutricionalButton.setOnClickListener {

            alimentoTabelaNutricional.calorias = java.lang.Double.parseDouble(binding.caloriaValue.text.toString().replace(".","").replace(",", "."))
            alimentoTabelaNutricional.carboidratos = java.lang.Double.parseDouble(binding.carboidratosValue.text.toString().replace(".","").replace(",", "."))
            alimentoTabelaNutricional.proteinas = java.lang.Double.parseDouble(binding.proteinasValue.text.toString().replace(".","").replace(",", "."))
            alimentoTabelaNutricional.gorduras = java.lang.Double.parseDouble(binding.gorduraValue.text.toString().replace(".","").replace(",", "."))
            alimentoTabelaNutricional.porcaoConsumida = java.lang.Double.parseDouble(binding.valorPorcaoValueText.text.toString())

            val sharedPreference = SharedPreference(context)
            val processoId = sharedPreference.getValueString(SharedIds.ID_USUARIO.name)
                ?: throw SalvarAlimentoUsuarioException("ProcessoId não encontrado no SharedPreference")

            progressBar.show(this.requireContext(), MessageLoading.MENSAGEM_SALVANDO.mensagem)

            alimentoService.salvarAlimentoUsuario(alimentoTabelaNutricional, args.idRefeicao,
                processoId,
                {
                    progressBar.dialog.dismiss()
                    val action =
                        IncluirAlimentoCadastradoFragmentDirections.
                            actionIncluirAlimentoCadastradoFragmentToListaAlimentosRefeicaoFragment2(
                                null, args.alimentoAvulso, args.horarioRefeicao, args.idRefeicao, args.nomeRefeicao)
                    view?.findNavController()?.navigate(action)
                },
                {
                    retornaTelaErroGenerico()
                })
        }

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

            caloriaValue.text = calcularQuantidadeGramas(porcaoDigitada, args.alimentoUsuario.caloriaPorcao)
            carboidratosValue.text = calcularQuantidadeGramas(porcaoDigitada, args.alimentoUsuario.carboidratoPorcao)
            proteinasValue.text = calcularQuantidadeGramas(porcaoDigitada, args.alimentoUsuario.proteinaPorcao)
            gorduraValue.text = calcularQuantidadeGramas(porcaoDigitada, args.alimentoUsuario.gorduraPorcao)
        }
    }

    private fun calcularQuantidadeGramas(valorPorcaoInserido: Double, valorMacronutriente: Double) : String {
        val resultado = (valorMacronutriente / args.alimentoUsuario.porcaoAlimento) * valorPorcaoInserido
        val decimal = DecimalFormat("#,###.#")
        return decimal.format(resultado)
    }

    private fun retornaTelaErroGenerico() {
        progressBar.dialog.dismiss()

        val action =
            IncluirAlimentoCadastradoFragmentDirections.
                actionIncluirAlimentoCadastradoFragmentToErroGenericoFragment()
        view?.findNavController()?.navigate(action)
    }

}
