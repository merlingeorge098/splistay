package com.example.splistay

import android.content.Context
import com.example.splistay.data.local.AppDatabase
import com.example.splistay.data.repository.SplitStayRepository
import com.example.splistay.network.ApiService
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceLocator {
    private var database: AppDatabase? = null
    private var repository: SplitStayRepository? = null

    private fun getDatabase(context: Context): AppDatabase {
        return database ?: synchronized(this) {
            val instance = AppDatabase.getDatabase(context)
            database = instance
            instance
        }
    }

    private val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.example.com/") // Mock base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    private fun getFirestore(): FirebaseFirestore? {
        return try {
            FirebaseFirestore.getInstance()
        } catch (e: Exception) {
            null
        }
    }

    fun provideRepository(context: Context): SplitStayRepository {
        return repository ?: synchronized(this) {
            val db = getDatabase(context)
            val firestore = getFirestore()
            val instance = SplitStayRepository(
                db.stayRoomDao(),
                db.roommateDao(),
                db.expenseDao(),
                db.choreDao(),
                db.settlementDao(),
                apiService,
                firestore
            )
            repository = instance
            instance
        }
    }
}
