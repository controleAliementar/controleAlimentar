package com.example.controlealimentar.adapter.impl


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.controlealimentar.R
import com.example.controlealimentar.adapter.IOnRefeicaoListFragmentInteractionListener
import com.example.controlealimentar.model.Refeicao
import kotlinx.android.synthetic.main.fragment_refeicao_item.view.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class RefeicaoItemRecyclerViewAdapter(
    private val mValues: List<Refeicao>,
    private val mListenerRefeicao: IOnRefeicaoListFragmentInteractionListener?
) : RecyclerView.Adapter<RefeicaoItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_refeicao_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        val decimal = DecimalFormat("##,###.#")

        if (item.id == "6ab66802-e7e5-4fb9-ba9a-6e85f44771a8"){
            holder.mHorarioRefeicaoTextView.visibility = View.GONE
            holder.mAbreviacaoHorario.visibility = View.GONE
        } else {
            holder.mHorarioRefeicaoTextView.text = convertLongToTime(item.horario)
        }

        holder.mTituloRefeicaoTextView.text = item.nome
        holder.mProteinaValueTextView.text = decimal.format(item.proteina) + " g"
        holder.mGorduraValueTextView.text = decimal.format(item.gordura) + " g"
        holder.mCarboidratoValueTextView.text = decimal.format(item.carboidrato) + " g"
        holder.mKcalValueTextView.text = decimal.format(item.caloria)

        holder.mView.setOnClickListener {
            mListenerRefeicao?.onRefeicaoListFragmentInteraction(item)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mTituloRefeicaoTextView: TextView = mView.tituloAlimentoTextView
        val mHorarioRefeicaoTextView: TextView = mView.horarioRefeicaoTextView
        val mKcalValueTextView: TextView = mView.kcalValueTextView
        val mProteinaValueTextView: TextView = mView.proteinaValueTextView
        val mCarboidratoValueTextView: TextView = mView.carboidratoValueTextView
        val mGorduraValueTextView: TextView = mView.gorduraValueTextView
        val mAbreviacaoHorario: TextView = mView.abreviacaoHoraTextView
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("HH:mm")
        return format.format(date)
    }
}
