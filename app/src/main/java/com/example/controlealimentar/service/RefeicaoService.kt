package com.example.controlealimentar.service

import com.example.controlealimentar.config.RetrofitConfig
import com.example.controlealimentar.exception.CadastrarUsuarioException
import com.example.controlealimentar.model.Refeicao

class RefeicaoService {

    private val retrofitConfig: RetrofitConfig = RetrofitConfig()

    fun buscarListaRefeicoes() : ArrayList<Refeicao> {

        val response = retrofitConfig.getRefeicaoGateway()!!
            .buscarRefeicaoIds()
            .execute()

        if (!response!!.isSuccessful){
            print(response.errorBody())
            throw CadastrarUsuarioException(response.message())
        }

        val listRefeicaoIdsGateway = response.body()

        val listRefeicao: ArrayList<Refeicao> = arrayListOf()

        listRefeicaoIdsGateway!!.forEach {
            val refeicao = Refeicao(
                it.id,
                it.nome
            )

            listRefeicao.add(refeicao)
        }

        return listRefeicao
    }

}