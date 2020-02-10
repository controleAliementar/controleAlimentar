package com.example.controlealimentar.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.controlealimentar.R
import com.example.controlealimentar.databinding.FragmentEditarMetasBinding

class EditarMetasFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding : FragmentEditarMetasBinding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_editar_metas, container, false)

        binding.salvarButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_editarMetasFragment_to_homeFragment))

        return binding.root
    }

}
