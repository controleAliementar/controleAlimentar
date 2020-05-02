package com.example.controlealimentar.navigation


import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.controlealimentar.adapter.IOnAlimentoListFragmentInteractionListener
import com.example.controlealimentar.adapter.impl.AlimentoItemRecyclerViewAdapter
import com.example.controlealimentar.databinding.FragmentListaBuscaAlimentosBinding
import com.example.controlealimentar.model.Alimento
import com.example.controlealimentar.model.Porcao
import com.example.controlealimentar.model.interfaces.OnLoadMoreListener
import com.example.controlealimentar.service.AlimentoService
import kotlinx.android.synthetic.main.fragment_lista_busca_alimentos.*




/**
 * A simple [Fragment] subclass.
 */
class ListaBuscaAlimentosFragment : Fragment(),
    IOnAlimentoListFragmentInteractionListener {

    private val alimentoService : AlimentoService =
        AlimentoService()
    val args: ListaBuscaAlimentosFragmentArgs by navArgs()
    lateinit var adapter: AlimentoItemRecyclerViewAdapter
    var listAlimentos = ArrayList<Alimento>()
    var ehUltimaPagina: Boolean = false
    var paginaAtual: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentListaBuscaAlimentosBinding = DataBindingUtil.inflate(
            inflater, com.example.controlealimentar.R.layout.fragment_lista_busca_alimentos, container, false
        )

        ehUltimaPagina = args.ehUltimaPagina

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val action =
                        ListaBuscaAlimentosFragmentDirections
                            .actionListaBuscaAlimentosFragmentToBuscarAlimentoFragment(null, args.idRefeicao)
                    view?.findNavController()?.navigate(action)
                }
            })

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        listAlimentos.addAll(args.listAlimentos)

        adapter = AlimentoItemRecyclerViewAdapter(
            recycleViewListAlimentos,
            listAlimentos,
            this.requireActivity(),
            this
        )

        recycleViewListAlimentos.setHasFixedSize(true)
        recycleViewListAlimentos.layoutManager = LinearLayoutManager(activity)
        recycleViewListAlimentos.adapter = adapter

        setOnLoadMoreListener()

    }

    override fun onAlimentoListFragmentInteraction(item: Alimento) {
        val action =
            ListaBuscaAlimentosFragmentDirections
                .actionListaBuscaAlimentosFragmentToBuscarAlimentoFragment(item, args.idRefeicao, args.alimentoAvulso)
        view?.findNavController()?.navigate(action)
    }

    private  fun setOnLoadMoreListener() {
        adapter.setOnLoadMoreListener(object :
            OnLoadMoreListener {
            override fun onLoadMore() {
                LoadMoreData()
            }
        })
    }


    private fun LoadMoreData(){

        if (!ehUltimaPagina){

            val proximaPagina = paginaAtual + 1

            val porcao = Porcao()
            listAlimentos.add(Alimento(porcao = porcao, nome = ""))

            recycleViewListAlimentos.post {
                adapter.notifyItemInserted(listAlimentos.size - 1)
            }

            Handler().postDelayed({

                listAlimentos.removeAt(listAlimentos.size - 1)
                adapter.notifyItemRemoved(listAlimentos.size)

                alimentoService.buscarAlimentoPaginado(args.nomeAlimento, proximaPagina,
                    {
                        listAlimentos.addAll(it.listAlimentos)
                        ehUltimaPagina = it.ehUltimaPagina

                        adapter.setLoaded()
                        adapter.notifyDataSetChanged()
                    },
                    {})

            },5000)

        }

    }

}
