package com.example.controlealimentar.adapter.impl


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.controlealimentar.R
import com.example.controlealimentar.adapter.IOnAlimentoListFragmentInteractionListener
import com.example.controlealimentar.model.Alimento
import kotlinx.android.synthetic.main.fragment_refeicao_item.view.*

class AlimentoItemRecyclerViewAdapter(
    private val mValues: Array<Alimento>,
    private val mListenerAlimento: IOnAlimentoListFragmentInteractionListener?
) : RecyclerView.Adapter<AlimentoItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_alimento_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mTituloRefeicaoTextView.text = item.nome

        holder.mView.setOnClickListener {
            mListenerAlimento?.onAlimentoListFragmentInteraction(item)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mTituloRefeicaoTextView: TextView = mView.tituloAlimentoTextView
    }
}
