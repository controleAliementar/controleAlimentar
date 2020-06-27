package com.example.controlealimentar.navigation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.controlealimentar.R
import com.example.controlealimentar.databinding.FragmentCadastrarDadosOcrBinding

/**
 * A simple [Fragment] subclass.
 */
class CadastrarDadosOcrFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentCadastrarDadosOcrBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_cadastrar_dados_ocr, container, false
        )

        binding.floatingActionButton.setOnClickListener {
            val action = CadastrarDadosOcrFragmentDirections.
                actionCadastrarDadosOcrFragmentToDicaFotoFragment()
            view?.findNavController()?.navigate(action)
        }


        return binding.root
    }


}
