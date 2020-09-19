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
import com.example.controlealimentar.service.UsuarioService
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

        binding.iconControleAlimentar.visibility = View.VISIBLE
        binding.titleControleAlimentar.visibility = View.VISIBLE

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Handler().postDelayed({
            binding.iconControleAlimentar.visibility = View.INVISIBLE
            binding.titleControleAlimentar.visibility = View.INVISIBLE
            decideFluxo()
        }, 4000)
    }


    private fun decideFluxo() {

        val sharedPreference = SharedPreference(context)

        val processoId = sharedPreference.getValueString(SharedIds.ID_USUARIO.name)

        if (processoId.isNullOrBlank()){
            findNavController().navigateSafe(R.id.action_loadingInicioAppFragment_to_cadastrarUsuarioFragment)
        } else {
            val metaDiariasService = MetaDiariasService()

            metaDiariasService.buscarMetaDiarias(processoId,
                {meta ->
                    if (meta.processoId.isBlank()) {
                        findNavController().navigateSafe(R.id.action_loadingInicioAppFragment_to_cadastrarMetasFragment)
                    }else {

                        val usuarioService = UsuarioService()

                        usuarioService.buscarFeedbackUsuario(processoId,
                            {

                                if(it.status.equals("P")){

                                    findNavController().navigateSafe(R.id.action_loadingInicioAppFragment_to_exibirFeedbackFragment2)

                                }

                                findNavController().navigateSafe(R.id.action_loadingInicioAppFragment_to_homeFragment)
                            },{
                                findNavController().navigateSafe(R.id.action_loadingInicioAppFragment_to_homeFragment)
                            })
                    }
                }, {
                    findNavController().navigateSafe(R.id.action_loadingInicioAppFragment_to_erroGenericoFragment)
                })
        }

    }

}
