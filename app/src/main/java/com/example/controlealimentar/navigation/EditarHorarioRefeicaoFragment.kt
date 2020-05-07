package com.example.controlealimentar.navigation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.controlealimentar.R
import com.example.controlealimentar.databinding.FragmentEditarHorarioRefeicaoBinding
import com.example.controlealimentar.exception.BuscarMetaDiariasException
import com.example.controlealimentar.model.enuns.MessageLoading
import com.example.controlealimentar.model.enuns.SharedIds
import com.example.controlealimentar.service.RefeicaoService
import com.example.controlealimentar.util.CustomProgressBar
import com.example.controlealimentar.util.SharedPreference
import kotlinx.android.synthetic.main.fragment_editar_horario_refeicao.*
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class EditarHorarioRefeicaoFragment : Fragment() {

    val args: EditarHorarioRefeicaoFragmentArgs by navArgs()
    val refeicaoService = RefeicaoService()
    val customProgressBar = CustomProgressBar()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentEditarHorarioRefeicaoBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_editar_horario_refeicao, container, false
        )

        binding.okButton.setOnClickListener {

            val hora = timePicker.currentHour
            val minuto = timePicker.currentMinute
            val horario = Timestamp(System.currentTimeMillis())

            horario.hours = hora
            horario.minutes = minuto

            val sharedPreference = SharedPreference(context)
            val processoId = sharedPreference.getValueString(SharedIds.ID_USUARIO.name)

            if (processoId.isNullOrBlank()){
                throw BuscarMetaDiariasException("ProcessoId não encontrado no sharedPreference")
            }

            customProgressBar.show(this.requireContext(), MessageLoading.MENSAGEM_SALVANDO.mensagem)
            refeicaoService.alterarHorarioRefeicao(processoId, args.idRefeicao, horario.time,
                {
                    customProgressBar.dialog.dismiss()
                    val action = EditarHorarioRefeicaoFragmentDirections
                        .actionHorarioRefeicaoFragmentToIncluirAlimentoFragment(args.idRefeicao, horario.time,
                            args.nomeRefeicao, args.alimentoAvulso)
                    view?.findNavController()?.popBackStack()
                },
                {
                    customProgressBar.dialog.dismiss()
                    val action = EditarHorarioRefeicaoFragmentDirections
                        .actionEditarHorarioRefeicaoFragmentToErroGenericoFragment()
                    view?.findNavController()?.navigate(action)
                })
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        timePicker.currentHour = Integer.valueOf(convertLongGetHour(args.horario))
        timePicker.currentMinute = Integer.valueOf(convertLongGetMinute(args.horario))
    }

    private fun convertLongGetHour(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("HH")
        return format.format(date)
    }

    private fun convertLongGetMinute(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("mm")
        return format.format(date)
    }


}
