package com.example.controlealimentar.navigation


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.controlealimentar.R
import com.example.controlealimentar.databinding.FragmentDicaFotoBinding


/**
 * A simple [Fragment] subclass.
 */
class DicaFotoFragment : Fragment() {

    val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentDicaFotoBinding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_dica_foto, container, false)

        binding.okButton.setOnClickListener { view ->
            tirarFoto()
        }

        return binding.root
    }



    private fun tirarFoto() {
        val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if ((requestCode == REQUEST_IMAGE_CAPTURE) && (resultCode == Activity.RESULT_OK)) {
            val action = DicaFotoFragmentDirections.actionDicaFotoFragmentToCadastrarDadosOcrFragment()
            view?.findNavController()?.navigate(action)
        }
    }
}
