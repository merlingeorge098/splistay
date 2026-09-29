package com.example.splistay.network

import retrofit2.http.GET
import retrofit2.http.Query

data class UpiVerificationResponse(
    val success: Boolean,
    val message: String,
    val formattedUpiLink: String?
)

interface ApiService {
    @GET("verify-split")
    suspend fun verifySplit(
        @Query("amount") amount: Double,
        @Query("roommates") roommates: String
    ): UpiVerificationResponse
}
