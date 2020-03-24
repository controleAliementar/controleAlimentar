package com.example.controlealimentar.navigation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.controlealimentar.R
import com.example.controlealimentar.databinding.FragmentLoadingInicioAppBinding
import com.example.controlealimentar.model.enuns.SharedIds
import com.example.controlealimentar.service.MetaDiariasService
import com.example.controlealimentar.util.SharedPreference


/**
 * A simple [Fragment] subclass.
 */
class LoadingInicioAppFragment : Fragment() {

    lateinit var binding: FragmentLoadingInicioAppBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_loading_inicio_app, container, false
        )

        binding.progressBar.visibility = View.VISIBLE
        binding.button.setOnClickListener {
            decideFluxo()
        }

        return binding.root
    }


    fun decideFluxo() {

        val sharedPreference = SharedPreference(context)

        val processoId = sharedPreference.getValueString(SharedIds.ID_USUARIO.name)


        if (processoId.isNullOrBlank()){
            val action = LoadingInicioAppFragmentDirections
                .actionLoadingInicioAppFragmentToCadastrarUsuarioFragment()
            view?.findNavController()?.navigate(action)
            return
        }

        val metaDiariasService = MetaDiariasService()

        val metaDiarias = metaDiariasService.buscarMetaDiarias(processoId)

        if (metaDiarias!!.processoId.isNullOrBlank()){
            val action = LoadingInicioAppFragmentDirections
                .actionLoadingInicioAppFragmentToSalvarMetasFragment()
            view?.findNavController()?.navigate(action)
            return
        }

        val action = LoadingInicioAppFragmentDirections
                .actionLoadingInicioAppFragmentToHomeFragment()
        view?.findNavController()?.navigate(action)
    }


}
