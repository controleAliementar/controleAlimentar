package com.example.controlealimentar.config

import com.example.controlealimentar.gateway.UsuarioGateway
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class RetrofitConfig {
    private var retrofit: Retrofit? = null

    init {
        this.retrofit = Retrofit.Builder()
            .baseUrl("https://controlealimentar.herokuapp.com/")
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
    }

    fun getUsuarioGateway() : UsuarioGateway? {
        return this.retrofit?.create(UsuarioGateway::class.java)
    }

}