package com.example.controlealimentar.navigation


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.controlealimentar.databinding.FragmentDicaFotoBinding






/**
 * A simple [Fragment] subclass.
 */
class DicaFotoFragment : Fragment() {

    private val REQUEST_IMAGE_CAPTURE = 1
    private val PermissionsRequestCode = 123

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentDicaFotoBinding = DataBindingUtil
            .inflate(inflater, com.example.controlealimentar.R.layout.fragment_dica_foto, container, false)

        binding.okButton.setOnClickListener { view ->
            if (ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), PermissionsRequestCode)
            } else {
                tirarFoto()
            }
        }

        return binding.root
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {

        when (requestCode) {
            PermissionsRequestCode -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    tirarFoto()
                }
                return
            }
        }
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
