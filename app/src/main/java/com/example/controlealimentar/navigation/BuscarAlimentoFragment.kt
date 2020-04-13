package com.example.controlealimentar.navigation

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
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
import com.example.controlealimentar.exception.SalvarAlimentoException
import com.example.controlealimentar.model.Alimento
import com.example.controlealimentar.model.SalvarAlimento
import com.example.controlealimentar.model.enuns.SharedIds
import com.example.controlealimentar.service.AlimentoService
import com.example.controlealimentar.util.CustomProgressBar
import com.example.controlealimentar.util.SharedPreference
import com.example.controlealimentar.util.ValidacaoFormatoMetas
import kotlinx.android.synthetic.main.fragment_buscar_alimento.*
import java.text.DecimalFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class BuscarAlimentoFragment : Fragment() {

    lateinit var binding : FragmentBuscarAlimentoBinding
    private val alimentoService : AlimentoService =
        AlimentoService()
    val metas = ValidacaoFormatoMetas()

    val args: BuscarAlimentoFragmentArgs by navArgs()
    val CEM: String = "100"
    var tipoPorcaoEscolhida: String = "Gramas"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater,
                com.example.controlealimentar.R.layout.fragment_buscar_alimento, container, false)


        requireActivity()
            .onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val action =
                        BuscarAlimentoFragmentDirections
                            .actionBuscarAlimentoFragmentToHomeFragment()
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
                preencherCamposMacronutrientes(tipoPorcaoEscolhida)
            }

            override fun onNothingSelected(parent: AdapterView<*>){}
        }

        salvarAlimentoButton.setOnClickListener {

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

            val salvarAlimento = SalvarAlimento(
                porcaoConsumida,
                calorias,
                carboidratos,
                proteinas,
                gorduras,
                false
            )

            SalvarAlimentoAsync(this.requireContext(), alimentoService, salvarAlimento, idAlimento, idRefeicao, processoId).execute()

            val action =
                BuscarAlimentoFragmentDirections
                    .actionBuscarAlimentoFragmentToHomeFragment()
            view?.findNavController()?.navigate(action)
        }

        buscarAlimentoButton.setOnClickListener {
            val alimento = alimentoText.text.toString()

            if (alimento.length < 3){
                alimentoText.setError("Mínimo de 3 caracteres")
                return@setOnClickListener
            }

            val execute = BuscarAlimentoAsync(
                this.requireContext(),
                binding,
                alimentoService,
                alimento
            ).execute()

           val listAlimentos = execute.get()

            if(listAlimentos.isNullOrEmpty()){
                binding.alimentoText.error = "Nenhum alimento encontrado"
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

            if (alimento.porcao != null) {
                tipoPorcaoAlimento = alimento.porcao.porcao
            }

            nomeAlimentoTextView.setText(args.alimento!!.nome)
            salvarAlimentoButton.isEnabled = true
            valorPorcaoText.setText(CEM)
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

        val list_of_items = arrayListOf("Gramas")

        if (!unidadeMedida.isNullOrBlank()){
            list_of_items.add(unidadeMedida)
        }

        val arrayAdapter =
            ArrayAdapter(this.requireContext(), R.layout.simple_spinner_item, list_of_items)
        arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

        preencherCamposMacronutrientes(tipoPorcaoEscolhida)
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

            if(args.alimento?.porcao != null && args.alimento!!.porcao.porcao == porcaoEscolhida){
                val quantidadeGramasPorcao = args.alimento!!.porcao.qtdGramas * porcaoDigitada
                caloriaValue.text = calcularQuantidadeGramas(quantidadeGramasPorcao, args.alimento!!.calorias)
                carboidratosValue.text = calcularQuantidadeGramas(quantidadeGramasPorcao, args.alimento!!.carboidratos)
                proteinasValue.text = calcularQuantidadeGramas(quantidadeGramasPorcao, args.alimento!!.proteinas)
                gorduraValue.text = calcularQuantidadeGramas(quantidadeGramasPorcao, args.alimento!!.gorduras)
            }else {
                caloriaValue.text = calcularQuantidadeGramas(porcaoDigitada, args.alimento!!.calorias)
                carboidratosValue.text = calcularQuantidadeGramas(porcaoDigitada, args.alimento!!.carboidratos)
                proteinasValue.text = calcularQuantidadeGramas(porcaoDigitada, args.alimento!!.proteinas)
                gorduraValue.text = calcularQuantidadeGramas(porcaoDigitada, args.alimento!!.gorduras)
            }

        }
    }

    @SuppressLint("StaticFieldLeak")
    class BuscarAlimentoAsync(var context: Context,
                              var binding : FragmentBuscarAlimentoBinding,
                              var alimentoService: AlimentoService,
                              var alimento: String) : AsyncTask<String, String, ArrayList<Alimento>>(){
        val progressBar = CustomProgressBar()

        @SuppressLint("WrongThread")
        override fun doInBackground(vararg params: String?): ArrayList<Alimento>{

            val listAlimentos: ArrayList<Alimento> = alimentoService.buscarAlimento(alimento)

            progressBar.dialog.dismiss()

            return listAlimentos
        }

        override fun onPreExecute() {
            super.onPreExecute()
            progressBar.show(context, "Buscando ...")
        }

    }

    @SuppressLint("StaticFieldLeak")
    class SalvarAlimentoAsync(var context: Context,
                              var alimentoService: AlimentoService,
                              var alimento: SalvarAlimento,
                              var idAlimento: String,
                              var idRefeicao: String,
                              var processoId: String) : AsyncTask<String, String, String>(){
        val progressBar = CustomProgressBar()

        @SuppressLint("WrongThread")
        override fun doInBackground(vararg params: String?) : String{

            alimentoService.salvarAlimento(alimento, idAlimento, idRefeicao, processoId)

            progressBar.dialog.dismiss()
            return ""
        }

        override fun onPreExecute() {
            super.onPreExecute()
            progressBar.show(context, "Salvando ...")
        }

    }

}
