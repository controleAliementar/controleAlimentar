package com.example.controlealimentar.navigation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.controlealimentar.R
import com.example.controlealimentar.databinding.FragmentErroGenericoBinding
import kotlinx.android.synthetic.main.fragment_erro_generico.*

/**
 * A simple [Fragment] subclass.
 */
class ErroGenericoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentErroGenericoBinding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_erro_generico, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tentarNovamenteButton.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }
    }


}
