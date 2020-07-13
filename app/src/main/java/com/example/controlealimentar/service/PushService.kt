package com.example.controlealimentar.service

import com.example.controlealimentar.model.enuns.SharedIds
import com.example.controlealimentar.util.SharedPreference
import com.google.firebase.messaging.FirebaseMessagingService

class PushService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {

        val sharedPreference = SharedPreference(baseContext)
        sharedPreference.save(SharedIds.TOKEN_FIREBASE.name, token)
    }
}