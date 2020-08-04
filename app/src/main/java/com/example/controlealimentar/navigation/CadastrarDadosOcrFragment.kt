package com.example.controlealimentar.navigation


import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.controlealimentar.R
import com.example.controlealimentar.databinding.FragmentCadastrarDadosOcrBinding
import com.example.controlealimentar.model.AlimentoUsuario
import com.example.controlealimentar.util.ValidacaoFormatoMetas
import kotlinx.android.synthetic.main.fragment_cadastrar_dados_ocr.*

/**
 * A simple [Fragment] subclass.
 */
class CadastrarDadosOcrFragment : Fragment() {

    val metas = ValidacaoFormatoMetas()
    val args: CadastrarDadosOcrFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentCadastrarDadosOcrBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_cadastrar_dados_ocr, container, false
        )

//        binding.floatingActionButton.setOnClickListener {
//            val action = CadastrarDadosOcrFragmentDirections.
//                actionCadastrarDadosOcrFragmentToDicaFotoFragment()
//            view?.findNavController()?.navigate(action)
//        }

        binding.salvarButton.setOnClickListener {

            if (java.lang.Double.parseDouble(porcaoEditText.text.toString()) <= 0){
                binding.porcaoEditText.error = "Valor inválido"
                return@setOnClickListener
            }

            val alimentoTabelaNutricional = AlimentoUsuario()
            alimentoTabelaNutricional.nomeAlimento = nomeEditText.text.toString()
            alimentoTabelaNutricional.caloriaPorcao = java.lang.Double.parseDouble(caloriaValueView.text.toString())
            alimentoTabelaNutricional.carboidratoPorcao = java.lang.Double.parseDouble(carboidratoText.text.toString())
            alimentoTabelaNutricional.proteinaPorcao = java.lang.Double.parseDouble(proteinaText.text.toString())
            alimentoTabelaNutricional.gorduraPorcao = java.lang.Double.parseDouble(gorduraText.text.toString())
            alimentoTabelaNutricional.porcaoAlimento = java.lang.Double.parseDouble(porcaoEditText.text.toString())
            alimentoTabelaNutricional.unidadePorcao = spinner.selectedItem.toString()

            val action = CadastrarDadosOcrFragmentDirections.
                actionCadastrarDadosOcrFragmentToIncluirAlimentoCadastradoFragment(
                    alimentoTabelaNutricional, args.idRefeicao, args.nomeRefeicao, args.horarioRefeicao, args.alimentoAvulso)
            view?.findNavController()?.navigate(action)
        }

        binding.helpIcon.setOnClickListener {
            val alertDialog = AlertDialog.Builder(requireContext())
                .setTitle("Ajuda")
                .setMessage("Refere-se a porção indicada na tabela nutricional, localizada na parte superior da mesma.")
            alertDialog.setPositiveButton(android.R.string.yes) { dialog, which -> }
            alertDialog.show()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listOptionsPorcao = arrayListOf("grama(s)", "mililitro(s)",
            "unidade(s)", "fatia(s)", "copo(s)")

        val arrayAdapter =
            ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_item, listOptionsPorcao)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

        validarCampos()

    }

    private fun validarCampos(){
        salvarButton.isEnabled = false

        var editTextNomeIsNull = true
        var editTextCaloriaIsNull = true
        var editTextCarboidratoIsNull = true
        var editTextProteinaIsNull = true
        var editTextGorduraIsNull = true
        var editTextPorcaoIsNull = true

        nomeEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {}

            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                editTextNomeIsNull = text.isNullOrBlank()
                habilitarBotao(editTextNomeIsNull, editTextCaloriaIsNull, editTextCarboidratoIsNull,
                    editTextProteinaIsNull, editTextGorduraIsNull, editTextPorcaoIsNull)
            }
        })

        caloriaValueView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {}

            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                editTextCaloriaIsNull = text.isNullOrBlank()
                habilitarBotao(editTextNomeIsNull, editTextCaloriaIsNull, editTextCarboidratoIsNull,
                    editTextProteinaIsNull, editTextGorduraIsNull, editTextPorcaoIsNull)
                metas.validar(caloriaValueView, text.toString())
            }
        })

        carboidratoText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {}

            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                editTextCarboidratoIsNull = text.isNullOrBlank()
                habilitarBotao(editTextNomeIsNull, editTextCaloriaIsNull, editTextCarboidratoIsNull,
                    editTextProteinaIsNull, editTextGorduraIsNull, editTextPorcaoIsNull)
                metas.validar(carboidratoText, text.toString())
            }
        })

        proteinaText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {}

            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                editTextProteinaIsNull = text.isNullOrBlank()
                habilitarBotao(editTextNomeIsNull, editTextCaloriaIsNull, editTextCarboidratoIsNull,
                    editTextProteinaIsNull, editTextGorduraIsNull, editTextPorcaoIsNull)
                metas.validar(proteinaText, text.toString())
            }
        })

        gorduraText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {}

            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                editTextGorduraIsNull = text.isNullOrBlank()
                habilitarBotao(editTextNomeIsNull, editTextCaloriaIsNull, editTextCarboidratoIsNull,
                    editTextProteinaIsNull, editTextGorduraIsNull, editTextPorcaoIsNull)
                metas.validar(gorduraText, text.toString())
            }
        })

        porcaoEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {}

            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                editTextPorcaoIsNull = text.isNullOrBlank()
                habilitarBotao(editTextNomeIsNull, editTextCaloriaIsNull, editTextCarboidratoIsNull,
                    editTextProteinaIsNull, editTextGorduraIsNull, editTextPorcaoIsNull)
                metas.validar(porcaoEditText, text.toString())
            }
        })
    }

    private fun habilitarBotao(editTextNomeIsNull: Boolean, editTextCaloriaIsNull: Boolean,
                               editTextCarboidratoIsNull: Boolean, editTextProteinaIsNull: Boolean,
                               editTextGorduraIsNull: Boolean, editTextPorcaoIsNull: Boolean) {
        salvarButton.isEnabled = !editTextNomeIsNull && !editTextCaloriaIsNull
                && !editTextCarboidratoIsNull && !editTextProteinaIsNull
                && !editTextGorduraIsNull && !editTextPorcaoIsNull
    }


}
