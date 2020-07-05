package com.example.controlealimentar.navigation


import android.os.Bundle
import android.os.StrictMode
import androidx.fragment.app.FragmentActivity
import com.example.controlealimentar.R
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId


class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val SDK_INT = android.os.Build.VERSION.SDK_INT
        if (SDK_INT > 8) {
            val policy = StrictMode.ThreadPolicy.Builder()
                .permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }
        setContentView(R.layout.activity_main)

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

            })

        if (savedInstanceState == null) {
            val fragment = LoadingInicioAppFragment()
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_loading_inicio_app, fragment)
                    .commit()
        }


    }

    override fun onResume() {
        super.onResume()
        GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this)
    }


}
