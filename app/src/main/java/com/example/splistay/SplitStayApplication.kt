package com.example.splistay

import android.app.Application
import android.util.Log
import com.example.splistay.data.local.AppDatabase
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplitStayApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            FirebaseApp.initializeApp(this)
            Log.d("SplitStayApplication", "Firebase initialized successfully")
        } catch (e: Exception) {
            Log.e("SplitStayApplication", "Failed to initialize Firebase: ${e.message}")
        }

        // Initialize Room database on startup and seed initial data
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = AppDatabase.getDatabase(applicationContext)
                db.openHelper.writableDatabase // Forces database creation on disk
                val repo = ServiceLocator.provideRepository(applicationContext)
                repo.ensureRoomAndUserExists("default_room")
                Log.d("SplitStayApplication", "Room database initialized and seeded successfully")
            } catch (e: Exception) {
                Log.e("SplitStayApplication", "Failed to initialize Room database: ${e.message}")
            }
        }
    }
}
