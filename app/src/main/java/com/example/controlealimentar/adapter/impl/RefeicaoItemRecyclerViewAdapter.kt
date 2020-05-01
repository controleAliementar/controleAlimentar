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
        holder.mTituloRefeicaoTextView.text = item.nome
        holder.mHorarioRefeicaoTextView.text = item.horario
        holder.mProteinaValueTextView.text = item.proteina.toString() + " g"
        holder.mGorduraValueTextView.text = item.gordura.toString() + " g"
        holder.mCarboidratoValueTextView.text = item.carboidrato.toString() + " g"
        holder.mKcalValueTextView.text = item.caloria.toString()

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
    }
}
