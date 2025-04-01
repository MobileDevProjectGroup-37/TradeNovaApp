package com.example.traderapp.data

import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    suspend fun register(email: String, password: String): Boolean
    suspend fun login(email: String, password: String): Boolean
    fun logout()
    fun getIsAuthenticatedFlow(): StateFlow<Boolean>
    suspend fun resetPassword(email: String): Boolean
}


