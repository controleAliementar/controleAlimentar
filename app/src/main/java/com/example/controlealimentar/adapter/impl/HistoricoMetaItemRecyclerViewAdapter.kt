package com.example.controlealimentar.adapter.impl


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.controlealimentar.R
import com.example.controlealimentar.adapter.IOnHistoricoMetaListFragmentInteractionListener
import com.example.controlealimentar.model.MetaDiarias
import kotlinx.android.synthetic.main.fragment_lista_metas_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class HistoricoMetaItemRecyclerViewAdapter(
    private val mValues: List<MetaDiarias>,
    private val mListenerMeta: IOnHistoricoMetaListFragmentInteractionListener?
) : RecyclerView.Adapter<HistoricoMetaItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_lista_metas_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]

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
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("HH:mm")
        return format.format(date)
    }
}
