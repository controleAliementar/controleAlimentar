package com.example.controlealimentar.navigation


import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.archit.calendardaterangepicker.customviews.CalendarListener
import com.example.controlealimentar.databinding.FragmentBuscarHistoricoMetasBinding
import com.example.controlealimentar.exception.BuscarMetaDiariasException
import com.example.controlealimentar.model.enuns.MessageLoading
import com.example.controlealimentar.model.enuns.SharedIds
import com.example.controlealimentar.service.MetaDiariasService
import com.example.controlealimentar.util.CustomProgressBar
import com.example.controlealimentar.util.SharedPreference
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * A simple [Fragment] subclass.
 */
class BuscarHistoricoMetasFragment : Fragment() {

    private val quantidadeLimiteDeDiasDaBusca: Int = 14
    val metaDiariaService = MetaDiariasService()
    val progressBar = CustomProgressBar()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentBuscarHistoricoMetasBinding = DataBindingUtil.inflate(
            inflater, com.example.controlealimentar.R.layout.fragment_buscar_historico_metas,
            container, false
        )

        val sharedPreference = SharedPreference(context)
        val processoId = sharedPreference.getValueString(SharedIds.ID_USUARIO.name)

        if (processoId.isNullOrBlank()){
            throw BuscarMetaDiariasException("ProcessoId não encontrado no sharedPreference")
        }

        progressBar.show(this.requireContext(), MessageLoading.MENSAGEM_GARREGANDO.mensagem)
        metaDiariaService.buscarMetaDiarias(processoId,
            {
                progressBar.dialog.dismiss()
                val dataInicioMeta = it.dataInclusao
                val form = SimpleDateFormat("yyyy-MM-dd")
                val date = form.parse(dataInicioMeta)
                val calendarInicio = Calendar.getInstance()
                calendarInicio.time = date
                binding.calendarDatePickerRange.setSelectableDateRange(calendarInicio, Calendar.getInstance())
            },
            {
                progressBar.dialog.dismiss()
                val action = BuscarHistoricoMetasFragmentDirections
                    .actionBuscarHistoricoMetasFragmentToErroGenericoFragment()
                view?.findNavController()?.navigate(action)
            })

        binding.calendarDatePickerRange.setCalendarListener(object : CalendarListener {
            override fun onFirstDateSelected(startDate: Calendar) {

            }

            override fun onDateRangeSelected(startDate: Calendar, endDate: Calendar) {
                val millionSeconds = endDate.timeInMillis - startDate.timeInMillis
                val countDays = TimeUnit.MILLISECONDS.toDays(millionSeconds)

                if (countDays > quantidadeLimiteDeDiasDaBusca){
                    binding.calendarDatePickerRange.resetAllSelectedViews()
                    val alertDialog = AlertDialog.Builder(requireContext())
                        .setTitle("Ops")
                        .setMessage("O período máximo de busca são de "+ (quantidadeLimiteDeDiasDaBusca + 1) +" dias")
                    alertDialog.setPositiveButton(android.R.string.yes) { dialog, which -> }
                    alertDialog.show()
                    return
                }

                val sharedPreference = SharedPreference(context)
                val processoId = sharedPreference.getValueString(SharedIds.ID_USUARIO.name)

                if (processoId.isNullOrBlank()){
                    throw BuscarMetaDiariasException("ProcessoId não encontrado no sharedPreference")
                }

                val form = SimpleDateFormat("yyyy-MM-dd")
                val dataInicio = form.format(startDate.time)
                val dataFim = form.format(endDate.time)

                metaDiariaService.buscarHistoricoMetaDiarias(processoId, dataInicio, dataFim,
                    {

                        if (it.isNullOrEmpty()){
                            binding.calendarDatePickerRange.resetAllSelectedViews()
                            val alertDialog = AlertDialog.Builder(requireContext())
                                .setTitle("Ops")
                                .setMessage("Nenhum histórico encontrado! Selecione outro período.")
                            alertDialog.setPositiveButton(android.R.string.yes) { dialog, which -> }
                            alertDialog.show()
                        } else {
                            val action = BuscarHistoricoMetasFragmentDirections
                                .actionBuscarHistoricoMetasFragmentToListaMetasFragment(it.toTypedArray())
                            view?.findNavController()?.navigate(action)
                        }
                    },
                    {
                        val action = BuscarHistoricoMetasFragmentDirections
                            .actionBuscarHistoricoMetasFragmentToErroGenericoFragment()
                        view?.findNavController()?.navigate(action)
                    })
            }
        })

        return binding.root
    }


}
