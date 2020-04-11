package com.example.controlealimentar.navigation

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.controlealimentar.R
import com.example.controlealimentar.databinding.FragmentCadastrarMetasBinding
import com.example.controlealimentar.exception.SalvarMetaDiariasException
import com.example.controlealimentar.model.MetaDiarias
import com.example.controlealimentar.model.enuns.SharedIds
import com.example.controlealimentar.service.MetaDiariasService
import com.example.controlealimentar.util.CustomProgressBar
import com.example.controlealimentar.util.SharedPreference
import com.example.controlealimentar.util.ValidacaoFormatoMetas

/**
 * A simple [Fragment] subclass.
 */
class CadastrarMetasFragment : Fragment() {

    private val metaDiariasService : MetaDiariasService =
        MetaDiariasService()
    val progressBar = CustomProgressBar()
    val metas = ValidacaoFormatoMetas()

    lateinit var binding : FragmentCadastrarMetasBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater,
                R.layout.fragment_cadastrar_metas, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.salvarButton.setOnClickListener {

            try {

                val sharedPreference = SharedPreference(context)
                val processoId : String? = sharedPreference.getValueString(SharedIds.ID_USUARIO.name)

                if (processoId.isNullOrBlank()){
                    throw SalvarMetaDiariasException("ProcessoId não encontrado no SharedPreference")
                }

                val gordura = java.lang.Double.parseDouble(binding.gorduraText.text.toString())
                val carboidrato = java.lang.Double.parseDouble(binding.carboidratoText.text.toString())
                val proteina = java.lang.Double.parseDouble(binding.proteinaText.text.toString())
                val calorias = java.lang.Double.parseDouble(binding.caloriaText.text.toString())

                val metaDiarias = MetaDiarias()
                metaDiarias.gorduras = gordura
                metaDiarias.carboidratos = carboidrato
                metaDiarias.proteinas = proteina
                metaDiarias.calorias = calorias
                metaDiarias.processoId = processoId

                SalvarMetasAsync(this.requireContext(), metaDiariasService, metaDiarias, processoId).execute()

                val action =
                    CadastrarMetasFragmentDirections.actionCadastrarMetasFragmentToHomeFragment()
                view?.findNavController()?.navigate(action)

            } catch (e : Exception){
                progressBar.dialog.dismiss()

                val action =
                    CadastrarMetasFragmentDirections.actionCadastrarMetasFragmentToErroGenericoFragment()
                view?.findNavController()?.navigate(action)
            }
        }

        binding.salvarButton.isEnabled = false

        var editText1IsNull = true
        var editText2IsNull = true
        var editText3IsNull = true
        var editText4IsNull = true

        binding.caloriaText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {}

            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                editText1IsNull = text.isNullOrBlank()
                habilitarBotao(editText1IsNull, editText2IsNull, editText3IsNull, editText4IsNull)
                metas.validar(binding.caloriaText, text.toString())
            }
        })

        binding.carboidratoText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {}

            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                editText2IsNull = text.isNullOrBlank()
                habilitarBotao(editText1IsNull, editText2IsNull, editText3IsNull, editText4IsNull)
                metas.validar(binding.carboidratoText, text.toString())
            }
        })

        binding.proteinaText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {}

            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                editText3IsNull = text.isNullOrBlank()
                habilitarBotao(editText1IsNull, editText2IsNull, editText3IsNull, editText4IsNull)
                metas.validar(binding.proteinaText, text.toString())
            }
        })

        binding.gorduraText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {}

            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                editText4IsNull = text.isNullOrBlank()
                habilitarBotao(editText1IsNull, editText2IsNull, editText3IsNull, editText4IsNull)
                metas.validar(binding.gorduraText, text.toString())
            }
        })

    }

    private fun habilitarBotao(editText1IsNull: Boolean, editText2IsNull: Boolean,
                               editText3IsNull: Boolean, editText4IsNull: Boolean) {
        binding.salvarButton.isEnabled = !editText1IsNull && !editText2IsNull
                && !editText3IsNull && !editText4IsNull
    }

    @SuppressLint("StaticFieldLeak")
    class SalvarMetasAsync(var context: Context,
                              var metaDiariasService: MetaDiariasService,
                              var metaDiarias: MetaDiarias,
                              var processoId: String) : AsyncTask<String, String, String>(){
        val progressBar = CustomProgressBar()

        @SuppressLint("WrongThread")
        override fun doInBackground(vararg params: String?) : String{

            metaDiariasService.salvarMetaDiarias(processoId, metaDiarias)

            progressBar.dialog.dismiss()
            return ""
        }

        override fun onPreExecute() {
            super.onPreExecute()
            progressBar.show(context, "Salvando ...")
        }

    }

}
