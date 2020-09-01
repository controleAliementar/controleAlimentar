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
import com.example.controlealimentar.service.MetaDiariasService

/**
 * A simple [Fragment] subclass.
 */
class HistoricoMetasFragment : Fragment() {

    private val metaDiariaService = MetaDiariasService()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentHistoricoMetasBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_historico_metas, container, false
        )

        binding.floatingActionButtonCalendar.setOnClickListener {
            val action = HistoricoMetasFragmentDirections
                .actionHistoricoMetasFragmentToBuscarHistoricoMetasFragment()
            view?.findNavController()?.navigate(action)
        }

        return binding.root

    }


}
