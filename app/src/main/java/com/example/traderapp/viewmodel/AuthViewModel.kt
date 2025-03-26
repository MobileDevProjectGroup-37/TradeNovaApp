package com.example.traderapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.traderapp.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var isAuthenticated = mutableStateOf(authRepository.isAuthenticated())
    var validationError = mutableStateOf<String?>(null)
    var touchIdEnabled = mutableStateOf(false)

    fun onEmailChange(newEmail: String) {
        email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        password.value = newPassword
    }

    fun onTouchIdChange(newValue: Boolean) {
        touchIdEnabled.value = newValue
    }

    fun isPasswordValid(): Boolean {
        return password.value.length >= 8
    }

    fun register(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            val success = authRepository.register(email.value, password.value)
            if (success) {
                isAuthenticated.value = true
                onSuccess()
            } else {
                validationError.value = "Ошибка регистрации"
                onFailure("Ошибка регистрации")
            }
        }
    }

    fun login(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            val success = authRepository.login(email.value, password.value)
            if (success) {
                isAuthenticated.value = true
                onSuccess()
            } else {
                validationError.value = "Ошибка входа"
                onFailure("Ошибка входа")
            }
        }
    }
    fun resetFields() {
        email.value = ""
        password.value = ""
        validationError.value = null
    }
    fun logout() {
        authRepository.logout()
        isAuthenticated.value = false
    }
}
