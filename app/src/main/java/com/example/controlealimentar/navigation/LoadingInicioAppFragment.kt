package com.example.controlealimentar.navigation


import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.controlealimentar.R
import com.example.controlealimentar.databinding.FragmentLoadingInicioAppBinding
import com.example.controlealimentar.extensions.navigateSafe
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

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({
            binding.progressBar.visibility = View.INVISIBLE
            decideFluxo()
        }, 1000)
    }


    private fun decideFluxo() {

        val sharedPreference = SharedPreference(context)

        val processoId = sharedPreference.getValueString(SharedIds.ID_USUARIO.name)

        var idToGo = R.id.action_loadingInicioAppFragment_to_homeFragment

        if (processoId.isNullOrBlank()){
            idToGo = R.id.action_loadingInicioAppFragment_to_cadastrarUsuarioFragment
        }

        val metaDiariasService = MetaDiariasService()

        processoId?.let {
            metaDiariasService.buscarMetaDiarias(it,
                {
                    if (it.processoId.isBlank()) {
                        idToGo = R.id.action_loadingInicioAppFragment_to_cadastrarMetasFragment
                    }
                }, {})

        }

        findNavController().navigateSafe(idToGo)

    }

}
