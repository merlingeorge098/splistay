package com.example.splistay

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp

class SplitStayApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            FirebaseApp.initializeApp(this)
            Log.d("SplitStayApplication", "Firebase initialized successfully")
        } catch (e: Exception) {
            Log.e("SplitStayApplication", "Failed to initialize Firebase: ${e.message}")
        }
    }
}
