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
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * A simple [Fragment] subclass.
 */
class BuscarHistoricoMetasFragment : Fragment() {

    private val quantidadeLimiteDeDiasDaBusca: Int = 14

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentBuscarHistoricoMetasBinding = DataBindingUtil.inflate(
            inflater, com.example.controlealimentar.R.layout.fragment_buscar_historico_metas, container, false
        )

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

            }
        })

        binding.buscarHistoricoButton.setOnClickListener {
            val action = BuscarHistoricoMetasFragmentDirections
                .actionBuscarHistoricoMetasFragmentToListaMetasFragment()
            view?.findNavController()?.navigate(action)
        }

        return binding.root
    }


}
