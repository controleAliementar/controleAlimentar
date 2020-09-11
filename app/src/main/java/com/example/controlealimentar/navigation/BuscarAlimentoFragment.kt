package com.example.controlealimentar.navigation

import android.R
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
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.controlealimentar.databinding.FragmentBuscarAlimentoBinding
import com.example.controlealimentar.exception.BuscarMetaDiariasException
import com.example.controlealimentar.exception.SalvarAlimentoException
import com.example.controlealimentar.model.SalvarAlimento
import com.example.controlealimentar.model.enuns.MessageLoading
import com.example.controlealimentar.model.enuns.SharedIds
import com.example.controlealimentar.service.AlimentoService
import com.example.controlealimentar.util.CustomProgressBar
import com.example.controlealimentar.util.SharedPreference
import com.example.controlealimentar.util.ValidacaoFormatoMetas
import kotlinx.android.synthetic.main.fragment_buscar_alimento.*
import java.text.DecimalFormat
import java.text.Normalizer


/**
 * A simple [Fragment] subclass.
 */
class BuscarAlimentoFragment : Fragment() {

    lateinit var binding : FragmentBuscarAlimentoBinding
    private val alimentoService : AlimentoService =
        AlimentoService()
    val metas = ValidacaoFormatoMetas()
    val progressBar = CustomProgressBar()
    val args: BuscarAlimentoFragmentArgs by navArgs()
    val CEM: String = "100"
    var tipoPorcaoEscolhida: String = "gramas"
    var page: Int = 0
    var size: Int = 10
    var idPorcao: String? = null
    val porcaoInicial : String = "gramas"
    val alimentoIseridoPeloUsuario = "606d816e-ed23-4304-8e93-7f56a5c8cb55"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater,
                com.example.controlealimentar.R.layout.fragment_buscar_alimento, container, false)


        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val action =
                        BuscarAlimentoFragmentDirections
                            .actionBuscarAlimentoFragmentToListaAlimentosRefeicaoFragment(
                                idRefeicao = args.idRefeicao,
                                horarioRefeicao = args.horarioRefeicao,
                                nomeRefeicao = args.nomeRefeicao)
                    view?.findNavController()?.navigate(action)
                }
            })

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
                salvarAlimentoButton.isEnabled = !text.isNullOrBlank() && args.alimento != null
            }
        })

        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long){
                tipoPorcaoEscolhida = parent.getItemAtPosition(position).toString()
                val regexPegaNumero = "[^0-9]".toRegex()
                if (!tipoPorcaoEscolhida.equals(porcaoInicial)){
                    val valorEscritoNaPorcao = args.alimento!!.porcao.porcao.replace(regexPegaNumero, "")

                    if (valorEscritoNaPorcao != ""){
                        valorPorcaoText.setText(valorEscritoNaPorcao)
                    }

                }
                preencherCamposMacronutrientes(tipoPorcaoEscolhida)
            }

            override fun onNothingSelected(parent: AdapterView<*>){}
        }

        salvarAlimentoButton.setOnClickListener {

            if (java.lang.Double.parseDouble(valorPorcaoText.text.toString()) <= 0){
                binding.valorPorcaoText.error = "Valor inválido"
                return@setOnClickListener
            }

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
            if (porcao == "gramas" || (args.alimento!!.id == alimentoIseridoPeloUsuario)){
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
                salvarAlimento.caloriasPorcao = args.alimento!!.calorias
                salvarAlimento.carboidratosPorcao = args.alimento!!.carboidratos
                salvarAlimento.proteinasPorcao = args.alimento!!.proteinas
                salvarAlimento.gordurasPorcao = args.alimento!!.gorduras
                salvarAlimento.porcaoAlimento = args.alimento!!.porcao.qtdGramas
                salvarAlimento.unidadePorcao = args.alimento!!.porcao.porcao
                salvarAlimento.nomeAlimento = args.alimento!!.nome
            }

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

        buscarAlimentoButton.setOnClickListener {
            val alimento = alimentoText.text.toString()

            if (alimento.length < 3){
                alimentoText.setError("Mínimo de 3 caracteres")
                return@setOnClickListener
            }

            val sharedPreference = SharedPreference(context)
            val processoId = sharedPreference.getValueString(SharedIds.ID_USUARIO.name)

            if (processoId.isNullOrBlank()){
                throw BuscarMetaDiariasException("ProcessoId não encontrado no sharedPreference")
            }

            progressBar.show(this.requireContext(), MessageLoading.MENSAGEM_BUSCANDO.mensagem)
            alimentoService.buscarAlimentoPaginado(alimento.unaccent(), processoId, page,
                {
                    if(it.listAlimentos.isNullOrEmpty()){
                        progressBar.dialog.dismiss()
                        binding.alimentoText.error = "Nenhum alimento encontrado"
                    }else{
                        progressBar.dialog.dismiss()
                        val action =
                            BuscarAlimentoFragmentDirections
                                .actionBuscarAlimentoFragmentToListaBuscaAlimentosFragment(
                                    it.listAlimentos.toTypedArray(),
                                    args.idRefeicao,
                                    it.ehUltimaPagina,
                                    alimento,
                                    args.alimentoAvulso,
                                    args.nomeRefeicao,
                                    args.horarioRefeicao)
                        view?.findNavController()?.navigate(action)
                    }
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

        salvarAlimentoButton.isEnabled = false

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

        val listOfItems = arrayListOf<String>()

        if (!unidadeMedida.isNullOrBlank()){
            val regexRemoveNumbers = "\\d".toRegex()
            listOfItems.add(unidadeMedida.replace(regexRemoveNumbers, ""))
        }

        if (args.alimento?.porcao != null && (args.alimento!!.porcao.id != "" || args.alimento!!.porcao.porcao == "")){
            listOfItems.add(porcaoInicial)
        }

        val arrayAdapter =
            ArrayAdapter(this.requireContext(), R.layout.simple_spinner_item, listOfItems)
        arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

        preencherCamposMacronutrientes(porcaoInicial)
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

    val REGEX_UNACCENT = "\\p{InCombiningDiacriticalMarks}+".toRegex()

    fun CharSequence.unaccent(): String {
        val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
        return REGEX_UNACCENT.replace(temp, "")
    }

}
