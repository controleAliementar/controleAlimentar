package com.example.controlealimentar.navigation


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.controlealimentar.databinding.FragmentDicaFotoBinding



/**
 * A simple [Fragment] subclass.
 */
class DicaFotoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentDicaFotoBinding = DataBindingUtil
            .inflate(inflater, com.example.controlealimentar.R.layout.fragment_dica_foto, container, false)

        binding.okButton.setOnClickListener { view ->
            abrirCamera()
        }

        return binding.root
    }



    private fun abrirCamera() {
        val REQUEST_IMAGE_CAPTURE = 1
        val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

}
