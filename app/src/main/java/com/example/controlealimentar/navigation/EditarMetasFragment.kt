package com.example.controlealimentar.navigation

import android.app.AlertDialog
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
import com.example.controlealimentar.databinding.FragmentEditarMetasBinding
import com.example.controlealimentar.exception.BuscarMetaDiariasException
import com.example.controlealimentar.exception.SalvarMetaDiariasException
import com.example.controlealimentar.model.MetaDiarias
import com.example.controlealimentar.model.enuns.MessageLoading
import com.example.controlealimentar.model.enuns.SharedIds
import com.example.controlealimentar.service.MetaDiariasService
import com.example.controlealimentar.util.CustomProgressBar
import com.example.controlealimentar.util.SharedPreference
import com.example.controlealimentar.util.ValidacaoFormatoMetas
import java.text.DecimalFormat

class EditarMetasFragment : Fragment() {

    lateinit var binding : FragmentEditarMetasBinding
    var metas = ValidacaoFormatoMetas()
    val progressBar = CustomProgressBar()
    val metaDiariasService = MetaDiariasService()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_editar_metas, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        try {
            val sharedPreference = SharedPreference(context)
            val processoId = sharedPreference.getValueString(SharedIds.ID_USUARIO.name)

            if (processoId.isNullOrBlank()){
                throw BuscarMetaDiariasException("ProcessoId não encontrado no sharedPreference")
            }

            progressBar.show(this.requireContext(), MessageLoading.MENSAGEM_GARREGANDO.mensagem)
            metaDiariasService.buscarMetaDiarias(processoId,
                {
                    binding.caloriaValueView.setText(it.calorias.toString())
                    binding.carboidratoText.setText(it.carboidratos.toString())
                    binding.proteinaText.setText(it.proteinas.toString())
                    binding.gorduraText.setText(it.gorduras.toString())
                    progressBar.dialog.dismiss()
                },
                {
                    retornarErroGenerico()
                })

        }catch (e : Exception){
            retornarErroGenerico()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.salvarButton.setOnClickListener{
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
                            EditarMetasFragmentDirections.actionEditarMetasFragmentToHomeFragment()
                        view?.findNavController()?.navigate(action)
                    },
                    {
                        retornarErroGenerico()
                    },
                    {
                        progressBar.dialog.dismiss()
                        val alertDialog = AlertDialog.Builder(requireContext())
                            .setTitle("Ops")
                            .setMessage("Meta não pode ser editada pois já existe alimento consumido")
                        alertDialog.setPositiveButton(android.R.string.yes) { dialog, which ->
                            val action =
                                EditarMetasFragmentDirections.actionEditarMetasFragmentToHomeFragment()
                            view?.findNavController()?.navigate(action)
                        }
                        alertDialog.show()
                    })

            } catch (e : Exception){
                retornarErroGenerico()
            }
        }

        binding.salvarButton.isEnabled = true

        var editText2IsNull = false
        var editText3IsNull = false
        var editText4IsNull = false

        binding.carboidratoText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {}

            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                editText2IsNull = text.isNullOrBlank()
                habilitarBotao(editText2IsNull, editText3IsNull, editText4IsNull)
                metas.validar(binding.carboidratoText, text.toString())
                calcularCalorias()
            }
        })

        binding.proteinaText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {}

            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                editText3IsNull = text.isNullOrBlank()
                habilitarBotao(editText2IsNull, editText3IsNull, editText4IsNull)
                metas.validar(binding.proteinaText, text.toString())
                calcularCalorias()
            }
        })

        binding.gorduraText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {}

            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                editText4IsNull = text.isNullOrBlank()
                habilitarBotao(editText2IsNull, editText3IsNull, editText4IsNull)
                metas.validar(binding.gorduraText, text.toString())
                calcularCalorias()
            }
        })

    }

    private fun retornarErroGenerico() {
        progressBar.dialog.dismiss()
        val action =
            EditarMetasFragmentDirections.actionEditarMetasFragmentToErroGenericoFragment()
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
