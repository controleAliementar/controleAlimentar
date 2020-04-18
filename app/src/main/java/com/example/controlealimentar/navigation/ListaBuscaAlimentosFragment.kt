package com.example.controlealimentar.navigation


import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import kotlinx.android.synthetic.main.fragment_lista_busca_alimentos.*




/**
 * A simple [Fragment] subclass.
 */
class ListaBuscaAlimentosFragment : Fragment(),
    IOnAlimentoListFragmentInteractionListener {

    val args: ListaBuscaAlimentosFragmentArgs by navArgs()
    lateinit var adapter: AlimentoItemRecyclerViewAdapter
    var listAlimentos = ArrayList<Alimento>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentListaBuscaAlimentosBinding = DataBindingUtil.inflate(
            inflater, com.example.controlealimentar.R.layout.fragment_lista_busca_alimentos, container, false
        )

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
                .actionListaBuscaAlimentosFragmentToBuscarAlimentoFragment(item, args.idRefeicao)
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

        val porcao = Porcao()
        listAlimentos.add(Alimento(porcao = porcao, nome = ""))

        recycleViewListAlimentos.post {
            adapter.notifyItemInserted(listAlimentos.size - 1)
        }

        Toast.makeText(this.requireContext(), "Ta carregando", Toast.LENGTH_LONG).show()

        Handler().postDelayed({

            listAlimentos.removeAt(listAlimentos.size - 1)
            adapter.notifyItemRemoved(listAlimentos.size)

            val porcao = Porcao()
            val alimento1 = Alimento(porcao = porcao, nome = "Parece que ta indo")
            val alimento2 = Alimento(porcao = porcao, nome = "Acho que foi hen")
            val alimento3 = Alimento(porcao = porcao, nome = "Se loko Cachoeira")
            val alimento7 = Alimento(porcao = porcao, nome = "Uhuuuuu")
            val alimento4 = Alimento(porcao = porcao, nome = "Ehhhhhh")
            val alimento5 = Alimento(porcao = porcao, nome = "Fodaaaaa")
            val alimento6 = Alimento(porcao = porcao, nome = "Demais")

            listAlimentos.addAll(arrayOf(alimento1,alimento2, alimento3, alimento4, alimento5, alimento6, alimento7))

            adapter.setLoaded()
            adapter.notifyDataSetChanged()


        },5000)

    }

}
