package com.example.controlealimentar.config

import com.example.controlealimentar.gateway.RefeicaoGateway
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit


class RetrofitMockConfig {
    private var retrofit: Retrofit? = null

    init {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(logging)
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