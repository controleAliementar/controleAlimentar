package com.example.controlealimentar.navigation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.controlealimentar.R
import com.example.controlealimentar.databinding.FragmentHomeBinding
import com.example.controlealimentar.model.enuns.Refeicoes

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

        binding.addAlimentoCafeManhaButton.setOnClickListener { view ->
            val action = HomeFragmentDirections
                .actionHomeFragmentToIncluirAlimentoFragment(Refeicoes.CAFE_MANHA.nome)
            view.findNavController().navigate(action)
        }

        binding.addAlimentoLancheManhaButton.setOnClickListener{ view ->
            val action = HomeFragmentDirections
                .actionHomeFragmentToIncluirAlimentoFragment(Refeicoes.LANCHE_MANHA.nome)
            view.findNavController().navigate(action)
        }

        binding.addAlimentoAlmocoButton.setOnClickListener{ view ->
            val action = HomeFragmentDirections
                .actionHomeFragmentToIncluirAlimentoFragment(Refeicoes.ALMOCO.nome)
            view.findNavController().navigate(action)
        }

        binding.addAlimentoLancheTardeButton.setOnClickListener{ view ->
            val action = HomeFragmentDirections
                .actionHomeFragmentToIncluirAlimentoFragment(Refeicoes.LANCHE_TARDE.nome)
            view.findNavController().navigate(action)
        }

        binding.addAlimentoJantaButton.setOnClickListener{ view ->
            val action = HomeFragmentDirections
                .actionHomeFragmentToIncluirAlimentoFragment(Refeicoes.JANTA.nome)
            view.findNavController().navigate(action)
        }

        binding.addAlimentoChaNoiteButton.setOnClickListener{ view ->
            val action = HomeFragmentDirections
                .actionHomeFragmentToIncluirAlimentoFragment(Refeicoes.CHA_NOITE.nome)
            view.findNavController().navigate(action)
        }


        return binding.root
    }


}
