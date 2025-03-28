package com.example.traderapp.data

interface AuthRepository {
    suspend fun register(email: String, password: String): Boolean
    suspend fun login(email: String, password: String): Boolean
    fun logout()
    fun isAuthenticated(): Boolean
}


