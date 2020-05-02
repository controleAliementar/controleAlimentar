package com.example.controlealimentar.config

import com.example.controlealimentar.gateway.AlimentoGateway
import com.example.controlealimentar.gateway.MetaDiariasGateway
import com.example.controlealimentar.gateway.RefeicaoGateway
import com.example.controlealimentar.gateway.UsuarioGateway
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit


class RetrofitConfig {
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
            .baseUrl("https://controlealimentar.herokuapp.com/")
            .addConverterFactory(JacksonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    fun getUsuarioGateway() : UsuarioGateway? {
        return this.retrofit?.create(UsuarioGateway::class.java)
    }

    fun getMetaDiariasGateway() : MetaDiariasGateway? {
        return this.retrofit?.create(MetaDiariasGateway::class.java)
    }

    fun getAlimentoGateway() : AlimentoGateway? {
        return this.retrofit?.create(AlimentoGateway::class.java)
    }

    fun getRefeicaoGateway() : RefeicaoGateway? {
        return this.retrofit?.create(RefeicaoGateway::class.java)
    }

}