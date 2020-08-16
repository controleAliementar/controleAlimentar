package com.example.controlealimentar.adapter.impl


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.controlealimentar.R
import com.example.controlealimentar.adapter.IOnHistoricoListRefeicoesFragmentInteractionListener
import com.example.controlealimentar.model.Refeicao
import java.text.SimpleDateFormat
import java.util.*

class HistoricoRefeicaoItemRecyclerViewAdapter(
    private val mValues: List<Refeicao>,
    private val mListenerMeta: IOnHistoricoListRefeicoesFragmentInteractionListener?
) : RecyclerView.Adapter<HistoricoRefeicaoItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_refeicao_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]

        holder.mView.setOnClickListener {
            mListenerMeta?.onHistoricoListRefeicoesFragmentInteraction(item)
        }

    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("HH:mm")
        return format.format(date)
    }
}
