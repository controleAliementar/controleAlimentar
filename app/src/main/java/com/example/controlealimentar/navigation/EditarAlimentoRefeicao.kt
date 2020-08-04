package com.example.controlealimentar.navigation


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private val alimentoService : AlimentoService = AlimentoService()
    val metas = ValidacaoFormatoMetas()
    val progressBar = CustomProgressBar()
    val args: EditarAlimentoRefeicaoArgs by navArgs()
    var tipoPorcaoEscolhida: String = "gramas"
    var idPorcao: String? = null
    val porcaoGramas : String = "gramas"
    val alimentoIseridoPeloUsuario = "606d816e-ed23-4304-8e93-7f56a5c8cb55"


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

                    if (valorEscritoNaPorcao != ""){
                        valorPorcaoText.setText(valorEscritoNaPorcao)
                    }

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

            val porcao = spinner.selectedItem
            if (porcao == porcaoGramas || args.alimento!!.id == alimentoIseridoPeloUsuario){
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

            if (args.alimento!!.id == alimentoIseridoPeloUsuario){
                salvarAlimento.unidadePorcao = tipoPorcaoEscolhida
            }

            progressBar.show(this.requireContext(), MessageLoading.MENSAGEM_SALVANDO.mensagem)
            alimentoService.editarAlimento(salvarAlimento, args.alimentoUsuario.idRegistro, processoId,
                {
                    progressBar.dialog.dismiss()
                    val action =
                        EditarAlimentoRefeicaoDirections
                            .actionEditarAlimentoRefeicaoToListaAlimentosRefeicaoFragment(
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
            alterarAlimentoButton.isEnabled = true
            criarSpinner(tipoPorcaoAlimento)

        }

    }

    private fun criarSpinner(unidadeMedida : String?){

        val regexRemoveNumbers = "\\d".toRegex()
        val listOfItems = arrayListOf<String>()
        val alimentoUsuario = args.alimentoUsuario

        if (!unidadeMedida.isNullOrBlank()) {
            listOfItems.add(unidadeMedida.replace(regexRemoveNumbers, ""))
        }

        if (args.alimento!!.id != alimentoIseridoPeloUsuario){
            listOfItems.add(porcaoGramas)
        }

        val arrayAdapter =
            ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_item, listOfItems)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

        spinner.setSelection(listOfItems.indexOf(alimentoUsuario.unidadePorcao.replace(regexRemoveNumbers, "")))

        valorPorcaoText.setText(alimentoUsuario.porcaoConsumida.toString())

        preencherCamposMacronutrientes(alimentoUsuario.unidadePorcao)

    }

    private fun calcularQuantidadeGramas(valorPorcaoInserido: Double, valorMacronutriente: Double) : String {
        val resultado = (valorMacronutriente / 100) * valorPorcaoInserido
        val decimal = DecimalFormat("#,###.#")
        return decimal.format(resultado)
    }

    private fun calcularMacronutrientesAlimentoIseridoPeloUsuario(valorPorcaoInserido: Double, valorMacronutriente: Double, valorPorcaoAlimento: Double) : String {
        val resultado = (valorMacronutriente * valorPorcaoInserido) / valorPorcaoAlimento
        val decimal = DecimalFormat("#,###.#")
        return decimal.format(resultado)
    }

    private fun preencherCamposMacronutrientes(porcaoEscolhida: String){
        if (args.alimento != null &&
            !valorPorcaoText.text.toString().isBlank()){

            val porcaoDigitada = java.lang.Double.parseDouble(valorPorcaoText.text.toString())

            if(args.alimento?.porcao != null && args.alimento!!.id == alimentoIseridoPeloUsuario){
                caloriaValue.text = calcularMacronutrientesAlimentoIseridoPeloUsuario(porcaoDigitada, args.alimento!!.calorias, args.alimento!!.porcao.qtdGramas)
                carboidratosValue.text = calcularMacronutrientesAlimentoIseridoPeloUsuario(porcaoDigitada, args.alimento!!.carboidratos, args.alimento!!.porcao.qtdGramas)
                proteinasValue.text = calcularMacronutrientesAlimentoIseridoPeloUsuario(porcaoDigitada, args.alimento!!.proteinas, args.alimento!!.porcao.qtdGramas)
                gorduraValue.text = calcularMacronutrientesAlimentoIseridoPeloUsuario(porcaoDigitada, args.alimento!!.gorduras, args.alimento!!.porcao.qtdGramas)
            }
            else if(args.alimento?.porcao != null && args.alimento!!.porcao.porcao.endsWith(porcaoEscolhida)){

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
