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
import com.example.controlealimentar.R
import com.example.controlealimentar.databinding.FragmentCadastrarMetasBinding
import com.example.controlealimentar.exception.SalvarMetaDiariasException
import com.example.controlealimentar.model.MetaDiarias
import com.example.controlealimentar.model.enuns.MessageLoading
import com.example.controlealimentar.model.enuns.SharedIds
import com.example.controlealimentar.service.MetaDiariasService
import com.example.controlealimentar.util.CustomProgressBar
import com.example.controlealimentar.util.SharedPreference
import com.example.controlealimentar.util.ValidacaoFormatoMetas
import java.text.DecimalFormat

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
                val calorias = java.lang.Double.parseDouble(binding.caloriaValueView.text.toString())

                val metaDiarias = MetaDiarias()
                metaDiarias.gorduras = gordura
                metaDiarias.carboidratos = carboidrato
                metaDiarias.proteinas = proteina
                metaDiarias.calorias = calorias
                metaDiarias.processoId = processoId

                progressBar.show(this.requireContext(), MessageLoading.MENSAGEM_SALVANDO.mensagem)

                metaDiariasService.salvarMetaDiarias(processoId, metaDiarias,
                    {
                        progressBar.dialog.dismiss()
                        val action =
                            CadastrarMetasFragmentDirections.actionCadastrarMetasFragmentToHomeFragment()
                        view?.findNavController()?.navigate(action)
                    },
                    {
                        retornaTelaErroGenerico()
                    },
                    {
                        retornaTelaErroGenerico()
                    })

            } catch (e : Exception){
                retornaTelaErroGenerico()
            }
        }

        binding.salvarButton.isEnabled = false

        var editText2IsNull = true
        var editText3IsNull = true
        var editText4IsNull = true

        binding.carboidratoText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {}

            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                editText2IsNull = text.isNullOrBlank()
                habilitarBotao(editText2IsNull, editText3IsNull, editText4IsNull)
                metas.validar(binding.carboidratoText, text.toString())
                calcularCalorias()
            }
        })

        binding.proteinaText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {}

            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                editText3IsNull = text.isNullOrBlank()
                habilitarBotao(editText2IsNull, editText3IsNull, editText4IsNull)
                metas.validar(binding.proteinaText, text.toString())
                calcularCalorias()
            }
        })

        binding.gorduraText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {}

            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                editText4IsNull = text.isNullOrBlank()
                habilitarBotao(editText2IsNull, editText3IsNull, editText4IsNull)
                metas.validar(binding.gorduraText, text.toString())
                calcularCalorias()
            }
        })

    }

    private fun retornaTelaErroGenerico() {
        progressBar.dialog.dismiss()

        val action =
            CadastrarMetasFragmentDirections.actionCadastrarMetasFragmentToErroGenericoFragment()
        view?.findNavController()?.navigate(action)
    }

    private fun habilitarBotao(editText2IsNull: Boolean,
                               editText3IsNull: Boolean, editText4IsNull: Boolean) {
        binding.salvarButton.isEnabled = !editText2IsNull
                && !editText3IsNull && !editText4IsNull
    }

    private fun calcularCalorias(){

        var gordura = 0.0
        if (!binding.gorduraText.text.toString().isBlank()){
            gordura = java.lang.Double.parseDouble(binding.gorduraText.text.toString())
        }

        var carboidrato = 0.0
        if (!binding.carboidratoText.text.toString().isBlank()){
            carboidrato = java.lang.Double.parseDouble(binding.carboidratoText.text.toString())
        }

        var proteina = 0.0
        if (!binding.proteinaText.text.toString().isBlank()){
            proteina = java.lang.Double.parseDouble(binding.proteinaText.text.toString())
        }

        val caloriasNoCarboidrato = calcularCaloriasNoCarboidrato(carboidrato)
        val caloriasNaGordura = calcularCaloriasNaGordura(gordura)
        val caloriasNaProteina = calcularCaloriasNaProteina(proteina)

        val calorias = caloriasNaGordura + caloriasNoCarboidrato + caloriasNaProteina
        val decimal = DecimalFormat("####.0")
        binding.caloriaValueView.text = decimal.format(calorias).replace(",", ".")
    }

    private fun calcularCaloriasNoCarboidrato(carboidrato: Double): Double {
        return carboidrato * 4
    }

    private fun calcularCaloriasNaProteina(proteina: Double): Double {
        return proteina * 4
    }

    private fun calcularCaloriasNaGordura(gordura: Double): Double {
        return gordura * 9
    }

}
