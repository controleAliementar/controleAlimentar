package com.example.controlealimentar.navigation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.controlealimentar.R
import com.example.controlealimentar.databinding.FragmentHistoricoMetasBinding
import com.example.controlealimentar.exception.BuscarMetaDiariasException
import com.example.controlealimentar.model.enuns.MessageLoading
import com.example.controlealimentar.model.enuns.SharedIds
import com.example.controlealimentar.service.MetaDiariasService
import com.example.controlealimentar.util.CustomProgressBar
import com.example.controlealimentar.util.SharedPreference

/**
 * A simple [Fragment] subclass.
 */
class HistoricoMetasFragment : Fragment() {

    private val metaDiariaService = MetaDiariasService()
    val progressBar = CustomProgressBar()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentHistoricoMetasBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_historico_metas, container, false
        )

        binding.floatingActionButtonCalendar.setOnClickListener {

            val sharedPreference = SharedPreference(context)
            val processoId = sharedPreference.getValueString(SharedIds.ID_USUARIO.name)

            if (processoId.isNullOrBlank()){
                throw BuscarMetaDiariasException("ProcessoId não encontrado no sharedPreference")
            }

            progressBar.show(this.requireContext(), MessageLoading.MENSAGEM_GARREGANDO.mensagem)
            metaDiariaService.buscarMetaDiarias(processoId,
                {
                    progressBar.dialog.dismiss()
                    val action = HistoricoMetasFragmentDirections
                        .actionHistoricoMetasFragmentToBuscarHistoricoMetasFragment(it.dataInclusao)
                    view?.findNavController()?.navigate(action)
                },
                {
                    progressBar.dialog.dismiss()
                    val action = HistoricoMetasFragmentDirections
                        .actionHistoricoMetasFragmentToErroGenericoFragment()
                    view?.findNavController()?.navigate(action)
                })

        }

        return binding.root

    }


}
