package com.example.controlealimentar.navigation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.controlealimentar.R
import com.example.controlealimentar.databinding.FragmentCadastrarUsuarioBinding

/**
 * A simple [Fragment] subclass.
 */
class CadastrarUsuarioFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentCadastrarUsuarioBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_cadastrar_usuario, container, false
        )

        binding.cadastrarUsuarioButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_cadastrarUsuarioFragment_to_editarMetasFragment)
        )

        return binding.root
    }


}
