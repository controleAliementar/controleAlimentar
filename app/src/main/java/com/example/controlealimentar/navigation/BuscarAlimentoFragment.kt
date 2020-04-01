package com.example.controlealimentar.navigation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.controlealimentar.R
import com.example.controlealimentar.databinding.FragmentBuscarAlimentoBinding
import com.example.controlealimentar.model.Alimento
import com.example.controlealimentar.service.AlimentoService
import kotlinx.android.synthetic.main.fragment_buscar_alimento.*

/**
 * A simple [Fragment] subclass.
 */
class BuscarAlimentoFragment : Fragment() {

    private val alimentoService : AlimentoService =
        AlimentoService()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentBuscarAlimentoBinding = DataBindingUtil
            .inflate(inflater,
                R.layout.fragment_buscar_alimento, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buscarAlimentoButton.setOnClickListener {

            val alimento = alimentoText.text.toString()

            val listAlimentos: ArrayList<Alimento> = alimentoService.buscarAlimento(alimento)

            if(listAlimentos.isNullOrEmpty()){
                alimentoText.error = "Nenhum alimento encontrado"
                return@setOnClickListener
            }

            val action =
                BuscarAlimentoFragmentDirections
                    .actionBuscarAlimentoFragmentToListaBuscaAlimentosFragment(listAlimentos.toTypedArray())
            view?.findNavController()?.navigate(action)

        }
    }


}
