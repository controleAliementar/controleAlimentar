package com.example.controlealimentar.navigation

import android.os.Bundle
import android.os.StrictMode
import androidx.appcompat.app.AppCompatActivity
import com.example.controlealimentar.R
import com.example.controlealimentar.model.enuns.SharedIds
import com.example.controlealimentar.service.MetaDiariasService
import com.example.controlealimentar.util.SharedPreference


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val SDK_INT = android.os.Build.VERSION.SDK_INT
        if (SDK_INT > 8) {
            val policy = StrictMode.ThreadPolicy.Builder()
                .permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        val sharedPreference = SharedPreference(context = applicationContext)

        val processoId = sharedPreference.getValueString(SharedIds.ID_USUARIO.name)

        if (processoId.isNullOrBlank()){
            return setContentView(R.layout.activity_main)
        }

        val metaDiariasService = MetaDiariasService()

        val metaDiarias = metaDiariasService.buscarMetaDiarias(processoId)

        if (!metaDiarias!!.processoId.isNullOrBlank()){
            setContentView(R.layout.fragment_home)
        }

        return setContentView(R.layout.fragment_editar_metas)
    }

}
