package com.example.controlealimentar.navigation


import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.controlealimentar.R

/**
 * A simple [Fragment] subclass.
 */
class SugerirAlimentoFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view: View = requireActivity().getLayoutInflater()
            .inflate(R.layout.fragment_sugerir_alimento, null)

        val alert = AlertDialog.Builder(activity)
        alert.setView(view)

        return alert.create()
    }


}
