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
import com.example.controlealimentar.model.enuns.SharedIds
import com.example.controlealimentar.service.MetaDiariasService
import com.example.controlealimentar.service.UsuarioService
import com.example.controlealimentar.util.SharedPreference
import kotlinx.android.synthetic.main.fragment_recuperar_cadastro.view.*

/**
 * A simple [Fragment] subclass.
 */
class RecuperarCadastroFragment : DialogFragment() {

    val usuarioService = UsuarioService()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view: View = requireActivity().getLayoutInflater()
            .inflate(R.layout.fragment_recuperar_cadastro, null)

        val alert = AlertDialog.Builder(activity)
        alert.setView(view)

        view.exit.setOnClickListener {
            dismiss()
        }

        view.buscarButton.setOnClickListener {

            val email = view.emailText.text.toString()

            if(!isEmailValido(email)){
                view.emailText.setError("E-mail inválido")
                return@setOnClickListener
            }

            usuarioService.buscarUsuarioPorEmail(email,
                {
                    if(it.id != ""){
                        decideFluxo(it.id)
                    }else {
                        view.emailText.setError("Nenhum usuário encontrado")
                        return@buscarUsuarioPorEmail
                    }

                },
                {
                    findNavController().navigateSafe(R.id.action_recuperarCadastroFragment_to_erroGenericoFragment)
                })

        }

        return alert.create()
    }

    private fun isEmailValido(email: String) : Boolean {
        val emailRegex = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+\$".toRegex()
        return emailRegex.containsMatchIn(email)
    }

    private fun decideFluxo(processoId: String) {

        val sharedPreference = SharedPreference(context)
        sharedPreference.save(SharedIds.ID_USUARIO.name, processoId)

        val metaDiariasService = MetaDiariasService()
        metaDiariasService.buscarMetaDiarias(processoId,
            {meta ->
                if (meta.processoId.isBlank()) {
                    findNavController().navigateSafe(R.id.action_recuperarCadastroFragment_to_cadastrarMetasFragment)
                }else {
                    findNavController().navigateSafe(R.id.action_recuperarCadastroFragment_to_homeFragment)
                }
            }, {
                findNavController().navigateSafe(R.id.action_recuperarCadastroFragment_to_erroGenericoFragment)
            })

    }


}
