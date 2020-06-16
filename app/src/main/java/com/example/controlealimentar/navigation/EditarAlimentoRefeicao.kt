package com.example.controlealimentar.navigation


import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.controlealimentar.R
import com.example.controlealimentar.databinding.FragmentEditarAlimentoRefeicaoBinding
import com.example.controlealimentar.exception.SalvarAlimentoException
import com.example.controlealimentar.model.SalvarAlimento
import com.example.controlealimentar.model.enuns.MessageLoading
import com.example.controlealimentar.model.enuns.SharedIds
import com.example.controlealimentar.service.AlimentoService
import com.example.controlealimentar.util.CustomProgressBar
import com.example.controlealimentar.util.SharedPreference
import com.example.controlealimentar.util.ValidacaoFormatoMetas
import kotlinx.android.synthetic.main.fragment_buscar_alimento.*
import kotlinx.android.synthetic.main.fragment_buscar_alimento.caloriaValue
import kotlinx.android.synthetic.main.fragment_buscar_alimento.carboidratosValue
import kotlinx.android.synthetic.main.fragment_buscar_alimento.gorduraValue
import kotlinx.android.synthetic.main.fragment_buscar_alimento.nomeAlimentoTextView
import kotlinx.android.synthetic.main.fragment_buscar_alimento.proteinasValue
import kotlinx.android.synthetic.main.fragment_buscar_alimento.spinner
import kotlinx.android.synthetic.main.fragment_buscar_alimento.valorPorcaoText
import kotlinx.android.synthetic.main.fragment_editar_alimento_refeicao.*
import java.text.DecimalFormat

/**
 * A simple [Fragment] subclass.
 */
class EditarAlimentoRefeicao : Fragment() {

    lateinit var binding : FragmentEditarAlimentoRefeicaoBinding
    private val alimentoService : AlimentoService =
        AlimentoService()
    val metas = ValidacaoFormatoMetas()
    val progressBar = CustomProgressBar()
    val args: BuscarAlimentoFragmentArgs by navArgs()
    val CEM: String = "100"
    var tipoPorcaoEscolhida: String = "gramas"
    var idPorcao: String? = null
    val porcaoGramas : String = "gramas"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil
            .inflate(inflater,
               R.layout.fragment_editar_alimento_refeicao, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        valorPorcaoText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                preencherCamposMacronutrientes(tipoPorcaoEscolhida)
            }

            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                metas.validar(valorPorcaoText, valorPorcaoText.text.toString())
                alterarAlimentoButton.isEnabled = !text.isNullOrBlank() && args.alimento != null
            }
        })

        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long){
                tipoPorcaoEscolhida = parent.getItemAtPosition(position).toString()
                val regexPegaNumero = "[^0-9]".toRegex()
                if (!tipoPorcaoEscolhida.equals(porcaoGramas)){
                    val valorEscritoNaPorcao = args.alimento!!.porcao.porcao.replace(regexPegaNumero, "")
                    valorPorcaoText.setText(valorEscritoNaPorcao)
                }
                preencherCamposMacronutrientes(tipoPorcaoEscolhida)
            }

            override fun onNothingSelected(parent: AdapterView<*>){}
        }

        alterarAlimentoButton.setOnClickListener {

            val sharedPreference = SharedPreference(context)
            val processoId = sharedPreference.getValueString(SharedIds.ID_USUARIO.name)
                ?: throw SalvarAlimentoException("ProcessoId não encontrado no SharedPreference")

            val porcaoConsumida = java.lang.Double.parseDouble(valorPorcaoText.text.toString())
            val calorias = java.lang.Double.parseDouble(caloriaValue.text.toString().replace(".","").replace(",", "."))
            val carboidratos = java.lang.Double.parseDouble(carboidratosValue.text.toString().replace(".","").replace(",", "."))
            val proteinas = java.lang.Double.parseDouble(proteinasValue.text.toString().replace(".","").replace(",", "."))
            val gorduras = java.lang.Double.parseDouble(gorduraValue.text.toString().replace(".","").replace(",", "."))
            val idAlimento = args.alimento!!.id
            val idRefeicao = args.idRefeicao

            val porcao = spinner.selectedItem
            if (porcao == porcaoGramas){
                idPorcao = null
            }

            val salvarAlimento = SalvarAlimento(
                porcaoConsumida,
                idPorcao,
                calorias,
                carboidratos,
                proteinas,
                gorduras,
                args.alimentoAvulso
            )

            progressBar.show(this.requireContext(), MessageLoading.MENSAGEM_SALVANDO.mensagem)
            alimentoService.salvarAlimento(salvarAlimento, idAlimento, idRefeicao, processoId,
                {
                    progressBar.dialog.dismiss()
                    val action =
                        BuscarAlimentoFragmentDirections
                            .actionBuscarAlimentoFragmentToListaAlimentosRefeicaoFragment(
                                idRefeicao = args.idRefeicao,
                                horarioRefeicao = args.horarioRefeicao,
                                nomeRefeicao = args.nomeRefeicao)
                    view?.findNavController()?.navigate(action)
                },
                {
                    retornarErroGenerico()
                })

        }

    }

    private fun retornarErroGenerico() {
        progressBar.dialog.dismiss()
        val action =
            BuscarAlimentoFragmentDirections
                .actionBuscarAlimentoFragmentToErroGenericoFragment()
        view?.findNavController()?.navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        alterarAlimentoButton.isEnabled = false

        if (args.alimento != null) {
            val alimento = args.alimento!!
            var tipoPorcaoAlimento : String? = null

            if (alimento.porcao != null) {
                tipoPorcaoAlimento = alimento.porcao.porcao
                idPorcao = alimento.porcao.id
            }

            nomeAlimentoTextView.setText(args.alimento!!.nome)
            salvarAlimentoButton.isEnabled = true
            valorPorcaoText.setText(CEM)
            criarSpinner(tipoPorcaoAlimento)
        }

        alimentoText.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
            if (false == hasFocus) {
                (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    alimentoText.getWindowToken(), 0
                )
            }
        })

    }

    private fun criarSpinner(unidadeMedida : String?){

        val list_of_items = arrayListOf(porcaoGramas)

        if (!unidadeMedida.isNullOrBlank()){
            val regexRemoveNumbers = "\\d".toRegex()
            list_of_items.add(unidadeMedida.replace(regexRemoveNumbers, ""))
        }

        val arrayAdapter =
            ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_item, list_of_items)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

        preencherCamposMacronutrientes(porcaoGramas)
    }

    private fun calcularQuantidadeGramas(valorPorcaoInserido: Double, valorMacronutriente: Double) : String {
        val resultado = (valorMacronutriente / 100) * valorPorcaoInserido
        val decimal = DecimalFormat("#,###.#")
        return decimal.format(resultado)
    }

    private fun preencherCamposMacronutrientes(porcaoEscolhida: String){
        if (args.alimento != null &&
            !valorPorcaoText.text.toString().isBlank()){

            val porcaoDigitada = java.lang.Double.parseDouble(valorPorcaoText.text.toString())

            if(args.alimento?.porcao != null && args.alimento!!.porcao.porcao.endsWith(porcaoEscolhida)){

                val regexPegaNumero = "[^0-9]".toRegex()
                val valorEscritoNaPorcao = args.alimento!!.porcao.porcao.replace(regexPegaNumero, "")
                var quantidadeGramasPorcao = args.alimento!!.porcao.qtdGramas

                if (!valorEscritoNaPorcao.isBlank()){
                    quantidadeGramasPorcao /= java.lang.Double.parseDouble(valorEscritoNaPorcao)
                }

                val quantidadeGramas = quantidadeGramasPorcao * porcaoDigitada
                caloriaValue.text = calcularQuantidadeGramas(quantidadeGramas, args.alimento!!.calorias)
                carboidratosValue.text = calcularQuantidadeGramas(quantidadeGramas, args.alimento!!.carboidratos)
                proteinasValue.text = calcularQuantidadeGramas(quantidadeGramas, args.alimento!!.proteinas)
                gorduraValue.text = calcularQuantidadeGramas(quantidadeGramas, args.alimento!!.gorduras)
            }else {
                caloriaValue.text = calcularQuantidadeGramas(porcaoDigitada, args.alimento!!.calorias)
                carboidratosValue.text = calcularQuantidadeGramas(porcaoDigitada, args.alimento!!.carboidratos)
                proteinasValue.text = calcularQuantidadeGramas(porcaoDigitada, args.alimento!!.proteinas)
                gorduraValue.text = calcularQuantidadeGramas(porcaoDigitada, args.alimento!!.gorduras)
            }

        }
    }

}
