package com.example.controlealimentar.adapter.impl


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.controlealimentar.adapter.IOnHistoricoMetaListFragmentInteractionListener
import com.example.controlealimentar.model.MetaDiariasHistorico
import kotlinx.android.synthetic.main.fragment_lista_metas_item.view.*
import java.text.SimpleDateFormat
import java.util.*



class HistoricoMetaItemRecyclerViewAdapter(
    private val mValues: Array<MetaDiariasHistorico>,
    private val mListenerMeta: IOnHistoricoMetaListFragmentInteractionListener?
) : RecyclerView.Adapter<HistoricoMetaItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(com.example.controlealimentar.R.layout.fragment_lista_metas_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]

        val textMetaStatus: String
        textMetaStatus = if (item.metaAtingida){
            "Meta atingida"
        } else {
            "Meta não atingida"
        }

        val date = SimpleDateFormat("yyyy-MM-dd").parse(item.dataReferencia)
        val dataMeta = SimpleDateFormat("dd/MM/yyyy").format(date)

        holder.mStatusMetaTextView.text = textMetaStatus
        holder.mDataMetaTextView.text = dataMeta

        holder.mView.setOnClickListener {
            mListenerMeta?.onHistoricoMetaListFragmentInteraction(item)
        }

        holder.mDetalharMetaIcon.setOnClickListener {
            mListenerMeta?.onHistoricoMetaDetalhadaIconeFragmentInteraction(item)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mDetalharMetaIcon: ImageView = mView.iconExpandirMeta
        val mStatusMetaTextView: TextView = mView.statusMetaTextView
        val mDataMetaTextView: TextView = mView.dataMetaTextView
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("HH:mm")
        return format.format(date)
    }
}
