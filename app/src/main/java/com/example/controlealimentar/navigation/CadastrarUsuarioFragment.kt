package com.example.controlealimentar.navigation


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.controlealimentar.databinding.FragmentCadastrarUsuarioBinding
import com.example.controlealimentar.model.Usuario
import com.example.controlealimentar.model.enuns.SharedIds
import com.example.controlealimentar.service.UsuarioService
import com.example.controlealimentar.util.Loading
import com.example.controlealimentar.util.SharedPreference


/**
 * A simple [Fragment] subclass.
 */
class CadastrarUsuarioFragment : Fragment() {

    private val usuarioService : UsuarioService =
        UsuarioService()

    lateinit var binding: FragmentCadastrarUsuarioBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, com.example.controlealimentar.R.layout.fragment_cadastrar_usuario, container, false
        )

        binding.cadastrarUsuarioButton.setOnClickListener{

            val loading = Loading(context)

            try {

                val name : String = binding.nameText.text.toString()
                val email : String = binding.emailText.text.toString()

                val sharedPreference = SharedPreference(context)

                if (!isEmailValido(email)){
                    binding.emailText.error = "Email inválido"
                    return@setOnClickListener
                }

                loading.criar()

                val usuario = Usuario()
                usuario.nome = name
                usuario.email = email

                val id =  usuarioService.salvarUsuario(usuario)

                sharedPreference.save(SharedIds.ID_USUARIO.name, id)

                loading.remover()

                val action = CadastrarUsuarioFragmentDirections
                    .actionCadastrarUsuarioFragmentToEditarMetasFragment()
                view?.findNavController()?.navigate(action)

            } catch (e : Exception) {
                loading.remover()

                val action = CadastrarUsuarioFragmentDirections
                    .actionCadastrarUsuarioFragmentToErroGenericoFragment()
                view?.findNavController()?.navigate(action)
            }
        }

        return binding.root
    }

    private fun isEmailValido(email: String) : Boolean {
        val emailRegex = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+\$".toRegex()
        return emailRegex.containsMatchIn(email)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.cadastrarUsuarioButton.isEnabled = false

        var editText1IsNull = true
        var editText2IsNull = true


        binding.nameText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                editText1IsNull = text.isNullOrBlank()
                habilitarBotao(editText1IsNull, editText2IsNull)
            }

            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                editText1IsNull = text.isNullOrBlank()
                habilitarBotao(editText1IsNull, editText2IsNull)
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                editText1IsNull = text.isNullOrBlank()
                habilitarBotao(editText1IsNull, editText2IsNull)
            }
        })

        binding.emailText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                editText2IsNull = text.isNullOrBlank()
                habilitarBotao(editText1IsNull, editText2IsNull)
            }

            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                editText2IsNull = text.isNullOrBlank()
                habilitarBotao(editText1IsNull, editText2IsNull)
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                editText2IsNull = text.isNullOrBlank()
                habilitarBotao(editText1IsNull, editText2IsNull)
            }
        })

    }

    private fun habilitarBotao(editText1IsNull: Boolean, editText2IsNull: Boolean) {
        binding.cadastrarUsuarioButton.isEnabled = !editText1IsNull && !editText2IsNull
    }

}
