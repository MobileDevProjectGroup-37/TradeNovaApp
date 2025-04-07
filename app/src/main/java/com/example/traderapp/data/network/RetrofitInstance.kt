package com.example.traderapp.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    val api: CryptoApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.binance.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(CryptoApi::class.java)
    }
}
