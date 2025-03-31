package com.example.traderapp.data.network

import com.example.traderapp.data.model.CryptoDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoApi {
    @GET("assets")
    suspend fun getCryptoList(): CryptoResponse
}
data class CryptoResponse(
    val data: List<CryptoDto>
)
