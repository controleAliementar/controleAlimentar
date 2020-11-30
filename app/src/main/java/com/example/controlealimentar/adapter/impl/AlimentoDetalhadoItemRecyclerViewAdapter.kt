package com.example.controlealimentar.adapter.impl


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.controlealimentar.R
import com.example.controlealimentar.adapter.IOnAlimentoDetalhadoListFragmentInteractionListener
import com.example.controlealimentar.model.AlimentoDetalhado
import kotlinx.android.synthetic.main.fragment_alimento_detalhado_item.view.*
import java.text.DecimalFormat

class AlimentoDetalhadoItemRecyclerViewAdapter(
    private val mValues: ArrayList<AlimentoDetalhado>,
    private val idRefeicao: String,
    private val mListenerAlimento: IOnAlimentoDetalhadoListFragmentInteractionListener?
) : RecyclerView.Adapter<AlimentoDetalhadoItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_alimento_detalhado_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val alimentoAvulsoId = "6ab66802-e7e5-4fb9-ba9a-6e85f44771a8"

        val item = mValues[position]
        val decimal = DecimalFormat("##,###.#")

        if (idRefeicao == alimentoAvulsoId) {
            holder.mIngeridoCheckBox.visibility = View.GONE
            holder.mExcluirAlimento.visibility = View.GONE
            holder.mEditarAlimento.visibility = View.GONE
        } else {
            holder.mIngeridoCheckBox.isChecked = item.alimentoIngerido

//            if (item.alimentoIngerido){
//                holder.mIngeridoCheckBox.isClickable = false
//            }

        }

        val regexRemoveNumbers = "\\d".toRegex()
        holder.mNomeAlimentoTextView.text = item.nomeAlimento
        holder.mPorcaoConsumidaTextView.text = decimal.format(item.porcaoConsumida)+ " " +item.unidadePorcao.replace(regexRemoveNumbers, "")
        holder.mProteinaValueTextView.text = decimal.format(item.proteinas) + " g"
        holder.mGorduraValueTextView.text = decimal.format(item.gorduras) + " g"
        holder.mCarboidratoValueTextView.text = decimal.format(item.carboidratos) + " g"
        holder.mKcalValueTextView.text = decimal.format(item.calorias)

        holder.mIngeridoCheckBox.setOnClickListener {
            mListenerAlimento?.onAlimentoDetalhadoListFragmentInteraction(item, it)
        }

        holder.mEditarAlimento.setOnClickListener {
            mListenerAlimento?.onAlimentoEditDetalhadoListFragmentInteraction(item)
        }

        holder.mExcluirAlimento.setOnClickListener {
            mListenerAlimento?.onAlimentoDeleteDetalhadoListFragmentInteraction(item)
        }

    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mNomeAlimentoTextView: TextView = mView.nomeAlimentoTextView
        val mPorcaoConsumidaTextView: TextView = mView.porcaoConsumidaTextView
        val mKcalValueTextView: TextView = mView.kcalDetalhadoValueTextView
        val mProteinaValueTextView: TextView = mView.proteinaDetalhadoValueTextView
        val mCarboidratoValueTextView: TextView = mView.carboidratoDetalhadoValueTextView
        val mGorduraValueTextView: TextView = mView.gorduraDetalhadoValueTextView
        val mIngeridoCheckBox: CheckBox = mView.ingeridoCheckBox
        val mEditarAlimento: ImageView = mView.edit_icon
        val mExcluirAlimento: ImageView = mView.delete_icon
    }
}
