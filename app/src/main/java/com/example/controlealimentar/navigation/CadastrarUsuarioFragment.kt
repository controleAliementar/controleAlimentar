package com.example.controlealimentar.navigation

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.controlealimentar.R
import com.example.controlealimentar.databinding.FragmentCadastrarUsuarioBinding
import com.example.controlealimentar.model.Usuario
import com.example.controlealimentar.model.enuns.MessageLoading
import com.example.controlealimentar.model.enuns.SharedIds
import com.example.controlealimentar.service.MetaDiariasService
import com.example.controlealimentar.service.UsuarioService
import com.example.controlealimentar.util.CustomProgressBar
import com.example.controlealimentar.util.SharedPreference
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.fragment_cadastrar_usuario.*
import java.util.*
import kotlin.system.exitProcess


/**
 * A simple [Fragment] subclass.
 */
class CadastrarUsuarioFragment : Fragment() {

    private val usuarioService : UsuarioService =
        UsuarioService()
    val progressBar = CustomProgressBar()
    lateinit var binding: FragmentCadastrarUsuarioBinding
    val callbackManager = CallbackManager.Factory.create()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_cadastrar_usuario, container, false
        )

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    exitProcess(0)
                }
            })


        binding.facebookButton.setReadPermissions(Arrays.asList("email", "public_profile"))
        binding.facebookButton.setFragment(this)
        binding.facebookButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {

                val request = GraphRequest.newMeRequest(
                    loginResult.accessToken
                ) { `object`, response ->

                    progressBar.show(context, MessageLoading.MENSAGEM_SALVANDO.mensagem)
                    val email = `object`.getString("email")
                    val name = `object`.getString("name")

                    usuarioService.buscarUsuarioPorEmail(email,
                        {usuario ->
                            val sharedPreference = SharedPreference(context)
                            var tokenFirebase = sharedPreference.getValueString(SharedIds.TOKEN_FIREBASE.name)

                            if (tokenFirebase.isNullOrBlank()){
                                tokenFirebase = ""
                            }

                            if (usuario.id != ""){

                                usuario.tokenFirebase = tokenFirebase
                                usuarioService.atualizarUsuario(usuario , usuario.id,
                                    {
                                        decideFluxo(usuario.id)
                                    },
                                    {
                                        erroGenerico()
                                    })
                            } else {
                                criarUsuario(name, email, tokenFirebase, sharedPreference, requireView())
                            }

                        },
                        {
                            erroGenerico()
                        })
                    progressBar.dialog.dismiss()

                }
                val parameters = Bundle()
                parameters.putString("fields", "id,name,email,gender,birthday")
                request.setParameters(parameters)
                request.executeAsync()

            }

            override fun onCancel() {
                Toast.makeText(requireContext(), "Cancelado login facebook", Toast.LENGTH_LONG).show()
            }

            override fun onError(exception: FacebookException) {
                erroGenerico()
            }
        })

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun isEmailValido(email: String) : Boolean {
        val emailRegex = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+\$".toRegex()
        return emailRegex.containsMatchIn(email)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        cadastrarUsuarioButton.setOnClickListener{

            try {

                val name : String = binding.nameText.text.toString()
                val email : String = binding.emailText.text.toString()

                val sharedPreference = SharedPreference(context)

                if (!isEmailValido(email)){
                    binding.emailText.error = "Email inválido"
                    return@setOnClickListener
                }

                var tokenFirebase = sharedPreference.getValueString(SharedIds.TOKEN_FIREBASE.name)

                if (tokenFirebase.isNullOrBlank()){
                    tokenFirebase = ""
                }

                progressBar.show(context, MessageLoading.MENSAGEM_SALVANDO.mensagem)
                criarUsuario(name, email, tokenFirebase, sharedPreference, view)

            } catch (e : Exception) {

                progressBar.dialog.dismiss()

                erroGenerico()
            }
        }

//        binding.recuperarCadastroLink.setOnClickListener {
//            val action = CadastrarUsuarioFragmentDirections
//                .actionCadastrarUsuarioFragmentToRecuperarCadastroFragment()
//            view?.findNavController()?.navigate(action)
//        }

    }

    private fun criarUsuario(
        name: String,
        email: String,
        tokenFirebase: String,
        sharedPreference: SharedPreference,
        view: View
    ) {
        val usuario = Usuario()
        usuario.nome = name
        usuario.email = email
        usuario.tokenFirebase = tokenFirebase

        usuarioService.salvarUsuario(usuario, {
            sharedPreference.save(SharedIds.ID_USUARIO.name, it)

            progressBar.dialog.dismiss()

            val action = CadastrarUsuarioFragmentDirections
                .actionCadastrarUsuarioFragmentToCadastrarMetasFragment()
            view?.findNavController()?.navigate(action)
        }, {
            erroGenerico()
        })
    }

    private fun habilitarBotao(editText1IsNull: Boolean, editText2IsNull: Boolean) {
        binding.cadastrarUsuarioButton.isEnabled = !editText1IsNull && !editText2IsNull
    }

    private fun erroGenerico(){
        val action = CadastrarUsuarioFragmentDirections
            .actionCadastrarUsuarioFragmentToErroGenericoFragment()
        view?.findNavController()?.navigate(action)
    }

    private fun decideFluxo(processoId: String) {

        val sharedPreference = SharedPreference(context)
        sharedPreference.save(SharedIds.ID_USUARIO.name, processoId)

        val metaDiariasService = MetaDiariasService()
        metaDiariasService.buscarMetaDiarias(processoId,
            {meta ->
                if (meta.processoId.isBlank()) {
                    val action = CadastrarUsuarioFragmentDirections
                        .actionCadastrarUsuarioFragmentToCadastrarMetasFragment()
                    view?.findNavController()?.navigate(action)
                }else {
                    val action = CadastrarUsuarioFragmentDirections
                        .actionCadastrarUsuarioFragmentToHomeFragment()
                    view?.findNavController()?.navigate(action)
                }
            }, {
                erroGenerico()
            })

    }

}
