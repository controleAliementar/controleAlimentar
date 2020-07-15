package com.example.controlealimentar.navigation


import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.controlealimentar.R
import com.example.controlealimentar.extensions.navigateSafe
import kotlinx.android.synthetic.main.fragment_exibir_feedback.view.*

/**
 * A simple [Fragment] subclass.
 */
class ExibirFeedbackFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view: View = requireActivity().getLayoutInflater()
            .inflate(R.layout.fragment_exibir_feedback, null)

        val alert = AlertDialog.Builder(activity)
        alert.setView(view)

        view.exit.setOnClickListener {
            findNavController().navigateSafe(R.id.action_exibirFeedbackFragment2_to_homeFragment)
        }

        return alert.create()
    }

}
