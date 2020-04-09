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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.controlealimentar.databinding.FragmentBuscarAlimentoBinding
import com.example.controlealimentar.exception.SalvarAlimentoException
import com.example.controlealimentar.model.Alimento
import com.example.controlealimentar.model.SalvarAlimento
import com.example.controlealimentar.model.enuns.SharedIds
import com.example.controlealimentar.service.AlimentoService
import com.example.controlealimentar.util.SharedPreference
import kotlinx.android.synthetic.main.fragment_buscar_alimento.*
import java.text.DecimalFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class BuscarAlimentoFragment : Fragment() {

    private val alimentoService : AlimentoService =
        AlimentoService()

    val args: BuscarAlimentoFragmentArgs by navArgs()
    val CEM: String = "100"
    var tipoPorcaoEscolhida: String = "Gramas"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentBuscarAlimentoBinding = DataBindingUtil
            .inflate(inflater,
                com.example.controlealimentar.R.layout.fragment_buscar_alimento, container, false)

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
                salvarAlimentoButton.isEnabled = !text.isNullOrBlank() && args.alimento != null
            }
        })

        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long){
                tipoPorcaoEscolhida = parent.getItemAtPosition(position).toString()
                preencherCamposMacronutrientes(tipoPorcaoEscolhida)
            }

            override fun onNothingSelected(parent: AdapterView<*>){}
        }

        salvarAlimentoButton.setOnClickListener {

            val sharedPreference = SharedPreference(context)
            val processoId = sharedPreference.getValueString(SharedIds.ID_USUARIO.name)
                ?: throw SalvarAlimentoException("ProcessoId não encontrado no SharedPreference")

            val porcaoConsumida = java.lang.Double.parseDouble(valorPorcaoText.text.toString())
            val calorias = java.lang.Double.parseDouble(caloriaValue.text.toString())
            val carboidratos = java.lang.Double.parseDouble(carboidratosValue.text.toString())
            val proteinas = java.lang.Double.parseDouble(proteinasValue.text.toString())
            val gorduras = java.lang.Double.parseDouble(gorduraValue.text.toString())
            val idAlimento = UUID.randomUUID().toString()
            val idRefeicao = args.idRefeicao

            val salvarAlimento = SalvarAlimento(
                porcaoConsumida,
                calorias,
                carboidratos,
                proteinas,
                gorduras,
                false
            )

            alimentoService.salvarAlimento(salvarAlimento, idAlimento, idRefeicao, processoId)
        }

        buscarAlimentoButton.setOnClickListener {

            val alimento = alimentoText.text.toString()

            val listAlimentos: ArrayList<Alimento> = alimentoService.buscarAlimento(alimento)

            if(listAlimentos.isNullOrEmpty()){
                alimentoText.error = "Nenhum alimento encontrado"
                return@setOnClickListener
            }

            val action =
                BuscarAlimentoFragmentDirections
                    .actionBuscarAlimentoFragmentToListaBuscaAlimentosFragment(listAlimentos.toTypedArray(), args.idRefeicao)
            view?.findNavController()?.navigate(action)

        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        salvarAlimentoButton.isEnabled = false

        if (args.alimento != null) {
            val alimento = args.alimento!!
            var tipoPorcaoAlimento : String? = null
            var valorPorcao : String = CEM

            if (alimento.porcao != null) {
                valorPorcao = alimento.porcao.qtdGramas.toString()
                tipoPorcaoAlimento = alimento.porcao.porcao
            }

            valorPorcaoText.setText(valorPorcao)
            criarSpinner(tipoPorcaoAlimento)
        }

        alimentoText.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
            if (false == hasFocus) {
                (context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    alimentoText.getWindowToken(), 0
                )
            }
        })

    }

    private fun criarSpinner(unidadeMedida : String?){

        val list_of_items = arrayOf("Gramas", unidadeMedida)

        val arrayAdapter =
            ArrayAdapter(this.requireContext(), R.layout.simple_spinner_item, list_of_items)
        arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

        preencherCamposMacronutrientes(tipoPorcaoEscolhida)
    }

    private fun calcularQuantidadeGramas(valorPorcaoInserido: Double, valorMacronutriente: Double, valorPorcao: Double) : String {
        val resultado = (valorMacronutriente / valorPorcao) * valorPorcaoInserido
        val decimal = DecimalFormat("#,###.#")
        return decimal.format(resultado)
    }

    private fun retornarPorcaoEscolhida(){

    }

    private fun preencherCamposMacronutrientes(porcaoEscolhida: String){
        if (args.alimento != null &&
            !valorPorcaoText.text.toString().isBlank()){

            val porcaoDigitada = java.lang.Double.parseDouble(valorPorcaoText.text.toString())

            if(args.alimento?.porcao != null && args.alimento!!.porcao.porcao == porcaoEscolhida){
                caloriaValue.text = calcularQuantidadeGramas(porcaoDigitada, args.alimento!!.calorias, args.alimento!!.porcao.qtdGramas)
                carboidratosValue.text = calcularQuantidadeGramas(porcaoDigitada, args.alimento!!.carboidratos, args.alimento!!.porcao.qtdGramas)
                proteinasValue.text = calcularQuantidadeGramas(porcaoDigitada, args.alimento!!.proteinas, args.alimento!!.porcao.qtdGramas)
                gorduraValue.text = calcularQuantidadeGramas(porcaoDigitada, args.alimento!!.gorduras, args.alimento!!.porcao.qtdGramas)
            }else {
                caloriaValue.text = calcularQuantidadeGramas(porcaoDigitada, args.alimento!!.calorias, 100.0)
                carboidratosValue.text = calcularQuantidadeGramas(porcaoDigitada, args.alimento!!.carboidratos, 100.0)
                proteinasValue.text = calcularQuantidadeGramas(porcaoDigitada, args.alimento!!.proteinas, 100.0)
                gorduraValue.text = calcularQuantidadeGramas(porcaoDigitada, args.alimento!!.gorduras, 100.0)
            }

        }
    }

}
