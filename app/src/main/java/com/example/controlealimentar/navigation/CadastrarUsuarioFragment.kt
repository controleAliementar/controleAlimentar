package com.example.controlealimentar.navigation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.controlealimentar.databinding.FragmentCadastrarUsuarioBinding
import com.example.controlealimentar.exception.CadastrarUsuarioException
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentCadastrarUsuarioBinding = DataBindingUtil.inflate(
            inflater, com.example.controlealimentar.R.layout.fragment_cadastrar_usuario, container, false
        )

        binding.cadastrarUsuarioButton.setOnClickListener{

            val sharedPreference = SharedPreference(context)
            val loading = Loading(context)

            loading.criar()
            try {
                val usuario = Usuario()
                usuario.nome = binding.nameText.text.toString()
                usuario.email = binding.emailText.text.toString()

                val id =  usuarioService.salvarUsuario(usuario, context)

                sharedPreference.save(SharedIds.ID_USUARIO.name, id)

                loading.remover()

                val action = CadastrarUsuarioFragmentDirections
                    .actionCadastrarUsuarioFragmentToEditarMetasFragment()
                view?.findNavController()?.navigate(action)
            } catch (e : CadastrarUsuarioException) {
                loading.remover()

                val action = CadastrarUsuarioFragmentDirections
                    .actionCadastrarUsuarioFragmentToErroGenericoFragment()
                view?.findNavController()?.navigate(action)
            }
        }

        return binding.root
    }

}
