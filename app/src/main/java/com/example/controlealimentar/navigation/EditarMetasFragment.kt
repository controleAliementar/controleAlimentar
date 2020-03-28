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
import com.example.controlealimentar.databinding.FragmentEditarMetasBinding
import com.example.controlealimentar.exception.BuscarMetaDiariasException
import com.example.controlealimentar.exception.SalvarMetaDiariasException
import com.example.controlealimentar.model.MetaDiarias
import com.example.controlealimentar.model.enuns.SharedIds
import com.example.controlealimentar.service.MetaDiariasService
import com.example.controlealimentar.util.Loading
import com.example.controlealimentar.util.SharedPreference
import com.example.controlealimentar.util.ValidacaoFormatoMetas

class EditarMetasFragment : Fragment() {

    lateinit var binding : FragmentEditarMetasBinding
    var metas = ValidacaoFormatoMetas()

    val metaDiariasService = MetaDiariasService()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_editar_metas, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val loading = Loading(context)
        try {

            loading.criar()

            val sharedPreference = SharedPreference(context)
            val processoId = sharedPreference.getValueString(SharedIds.ID_USUARIO.name)

            if (processoId.isNullOrBlank()){
                throw BuscarMetaDiariasException("ProcessoId não encontrado no sharedPreference")
            }

            val metaDiarias = metaDiariasService.buscarMetaDiarias(processoId)

            if (metaDiarias != null) {
                binding.caloriaText.setText(metaDiarias.calorias.toString())
                binding.carboidratoText.setText(metaDiarias.carboidratos.toString())
                binding.proteinaText.setText(metaDiarias.proteinas.toString())
                binding.gorduraText.setText(metaDiarias.gorduras.toString())
            }

            loading.remover()
        }catch (e : Exception){
            loading.remover()

            val action =
                EditarMetasFragmentDirections.actionEditarMetasFragmentToErroGenericoFragment()
            view?.findNavController()?.navigate(action)
        }


        binding.salvarButton.setOnClickListener{

            loading.criar()

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

                metaDiariasService.salvarMetaDiarias(processoId, metaDiarias)

                loading.remover()

                val action =
                    EditarMetasFragmentDirections.actionEditarMetasFragmentToHomeFragment()
                view?.findNavController()?.navigate(action)
            } catch (e : Exception){
                loading.remover()

                val action =
                    EditarMetasFragmentDirections.actionEditarMetasFragmentToErroGenericoFragment()
                view?.findNavController()?.navigate(action)
            }

        }

        binding.salvarButton.isEnabled = true

        var editText1IsNull = false
        var editText2IsNull = false
        var editText3IsNull = false
        var editText4IsNull = false

        binding.caloriaText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {}

            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                editText1IsNull = text.isNullOrBlank()
                habilitarBotao(editText1IsNull, editText2IsNull, editText3IsNull, editText4IsNull)
                metas.validar(binding.caloriaText, text.toString())
            }
        })

        binding.carboidratoText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {}

            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                editText2IsNull = text.isNullOrBlank()
                habilitarBotao(editText1IsNull, editText2IsNull, editText3IsNull, editText4IsNull)
                metas.validar(binding.carboidratoText, text.toString())
            }
        })

        binding.proteinaText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {}

            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                editText3IsNull = text.isNullOrBlank()
                habilitarBotao(editText1IsNull, editText2IsNull, editText3IsNull, editText4IsNull)
                metas.validar(binding.proteinaText, text.toString())
            }
        })

        binding.gorduraText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {}

            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

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

}
