package com.example.controlealimentar.navigation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.controlealimentar.R
import com.example.controlealimentar.databinding.FragmentExibirMetaDetalhadaBinding

/**
 * A simple [Fragment] subclass.
 */
class ExibirMetaDetalhadaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentExibirMetaDetalhadaBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_exibir_meta_detalhada, container, false
        )

        binding.iconExpandirMeta.setOnClickListener {
            val action = ExibirMetaDetalhadaFragmentDirections
                    .actionExibirMetaDetalhadaFragmentToListaRefeicoesHistoricoMetasFragment()
            view?.findNavController()?.navigate(action)
        }

        binding.visualizarRefeicoesText.setOnClickListener {
            val action = ExibirMetaDetalhadaFragmentDirections
                .actionExibirMetaDetalhadaFragmentToListaRefeicoesHistoricoMetasFragment()
            view?.findNavController()?.navigate(action)
        }

        return binding.root
    }


}
