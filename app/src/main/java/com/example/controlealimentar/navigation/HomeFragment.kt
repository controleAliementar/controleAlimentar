package com.example.controlealimentar.navigation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.controlealimentar.R
import com.example.controlealimentar.databinding.FragmentHomeBinding

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentHomeBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false
        )

        binding.editarMetasbutton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_editarMetasFragment))

        binding.addAlimentoCafeManhaButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_incluirAlimentoFragment))

        binding.addAlimentoLancheManhaButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_incluirAlimentoFragment))

        binding.addAlimentoAlmocoButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_incluirAlimentoFragment))

        binding.addAlimentoLancheTardeButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_incluirAlimentoFragment))

        binding.addAlimentoJantaButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_incluirAlimentoFragment))

        binding.addAlimentoChaNoiteButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_incluirAlimentoFragment))


        return binding.root
    }


}
