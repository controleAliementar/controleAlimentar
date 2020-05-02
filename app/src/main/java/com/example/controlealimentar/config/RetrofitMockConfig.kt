package com.example.controlealimentar.config

import com.example.controlealimentar.gateway.RefeicaoGateway
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit


class RetrofitMockConfig {
    private var retrofit: Retrofit? = null

    init {
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        this.retrofit = Retrofit.Builder()
            .baseUrl("http://www.mocky.io/v2/")
            .addConverterFactory(JacksonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    fun getRefeicaoGateway() : RefeicaoGateway? {
        return this.retrofit?.create(RefeicaoGateway::class.java)
    }

}