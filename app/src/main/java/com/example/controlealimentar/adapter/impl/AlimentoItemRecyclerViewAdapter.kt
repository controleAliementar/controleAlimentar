package com.example.controlealimentar.adapter.impl

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.controlealimentar.adapter.IOnAlimentoListFragmentInteractionListener
import com.example.controlealimentar.model.Alimento
import com.example.controlealimentar.model.interfaces.OnLoadMoreListener
import kotlinx.android.synthetic.main.fragment_alimento_item.view.*



@Suppress("DEPRECATED_IDENTITY_EQUALS")
class AlimentoItemRecyclerViewAdapter(
    recyclerView: RecyclerView,
    alimentos: ArrayList<Alimento>,
    activity: Activity,
    private val mListenerAlimento: IOnAlimentoListFragmentInteractionListener?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var mcontext: Context
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    private var isLoading: Boolean = false
    private var activity: Activity? = activity
    private var listAlimentos: ArrayList<Alimento>? = alimentos
    private var visibleThreshold = 15
    private var lastVisibleItem: Int = 0
    private var totalItemCount: Int = 0

    init {
        val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount =  linearLayoutManager.getItemCount()
                lastVisibleItem = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener!!.onLoadMore()
                    }
                    isLoading = true
                }
            }
        })
    }

    override fun getItemViewType(position: Int): Int {
        return (if (listAlimentos!!.get(position).nome != "") VIEW_TYPE_ITEM else VIEW_TYPE_LOADING).toInt()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mcontext = parent.context
        return if (VIEW_TYPE_ITEM == viewType) {
            val view = LayoutInflater.from(activity)
                .inflate(com.example.controlealimentar.R.layout.fragment_alimento_item, parent, false)
            UserViewHolder(view)
        } else  {
            val view = LayoutInflater.from(activity).inflate(com.example.controlealimentar.R.layout.progress_bar_recycle_view, parent, false)
            LoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is UserViewHolder) {
            val item = listAlimentos!!.get(position)
            holder.mTituloRefeicaoTextView.text = item.nome

            holder.itemView.setOnClickListener {
                mListenerAlimento?.onAlimentoListFragmentInteraction(item)
            }
        } else if (holder is LoadingViewHolder) {
            holder.progressBar.isIndeterminate = true
        }
    }

    override fun getItemCount(): Int = listAlimentos!!.size

    fun setLoaded() {
        isLoading = false
    }


    // "Loading item" ViewHolder
    private inner class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var progressBar: ProgressBar = view.findViewById<View>(com.example.controlealimentar.R.id.progressbar_recycle_view) as ProgressBar
    }

    // "Normal item" ViewHolder
    private inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mTituloRefeicaoTextView: TextView = view.tituloListaAlimentoTextView
    }

    private var onLoadMoreListener: OnLoadMoreListener? = null
    fun setOnLoadMoreListener(mOnLoadMoreListener: OnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener
    }

}
