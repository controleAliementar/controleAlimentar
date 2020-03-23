package com.example.controlealimentar.navigation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.controlealimentar.R
import com.example.controlealimentar.databinding.FragmentSalvarMetasBinding
import com.example.controlealimentar.exception.SalvarMetaDiariasException
import com.example.controlealimentar.model.MetaDiarias
import com.example.controlealimentar.model.enuns.SharedIds
import com.example.controlealimentar.service.MetaDiariasService
import com.example.controlealimentar.util.Loading
import com.example.controlealimentar.util.SharedPreference

/**
 * A simple [Fragment] subclass.
 */
class SalvarMetasFragment : Fragment() {

    private val metaDiariasService : MetaDiariasService =
        MetaDiariasService()

    lateinit var binding : FragmentSalvarMetasBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater,
                R.layout.fragment_salvar_metas, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.salvarButton.setOnClickListener {

            val loading = Loading(context)

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
                    SalvarMetasFragmentDirections.actionSalvarMetasFragmentToHomeFragment()
                view?.findNavController()?.navigate(action)
            } catch (e : Exception){
                loading.remover()

                val action =
                    SalvarMetasFragmentDirections.actionSalvarMetasFragmentToErroGenericoFragment()
                view?.findNavController()?.navigate(action)
            }
        }

    }


}
