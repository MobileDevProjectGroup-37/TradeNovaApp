package com.example.traderapp.data.network

import com.example.traderapp.data.UserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSession @Inject constructor() {
    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> = _userData

    fun updateUser(user: UserData) {
        _userData.value = user
    }

    fun clear() {
        _userData.value = null
    }
}
