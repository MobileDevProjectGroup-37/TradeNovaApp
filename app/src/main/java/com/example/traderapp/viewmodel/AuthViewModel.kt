package com.example.traderapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.traderapp.data.AuthRepository
import com.example.traderapp.data.network.UserSession
import com.example.traderapp.data.network.WebSocketClient
import com.example.traderapp.utils.AuthPreferences
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.Firebase
import com.google.firebase.functions.functions
import com.google.firebase.functions.ktx.functions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val authPreferences: AuthPreferences,
    private val userSession: UserSession,
    private val webSocketClient: WebSocketClient

    ) : ViewModel() {
    var otpCode = mutableStateOf("")
    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var validationError = mutableStateOf<String?>(null)
    var touchIdEnabled = mutableStateOf(false)
    var confirmPassword = mutableStateOf("")

    // State to track if the user is logged in
    private val _isLoggedIn = MutableStateFlow(authRepository.getIsAuthenticatedFlow().value)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn. asStateFlow()

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
                _isLoggedIn.value = true
                onSuccess()
            } else {
                validationError.value = "Error during registration"
                onFailure("Error during registration")
            }
        }
    }

    fun login(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            val success = authRepository.login(email.value, password.value)
            if (success) {
                authPreferences.saveIsAuthenticated(true)
                _isLoggedIn.value = true
                onSuccess()
            } else {
                validationError.value = "Error during login"
                onFailure("Error during login")
            }
        }
    }
    fun onConfirmPasswordChange(newConfirmPassword: String) {
        confirmPassword.value = newConfirmPassword
    }
    fun isConfirmPasswordValid(): Boolean {
        return password.value == confirmPassword.value
    }


    fun logout() {
        authRepository.logout()
        authPreferences.clear()
        _isLoggedIn.value = false
        userSession.clear()
        webSocketClient.disconnect()
    }

    fun resetFields() {
        email.value = ""
        password.value = ""
        validationError.value = null
    }
    fun sendOtp(
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val result = Firebase.functions
                    .getHttpsCallable("sendOtp")
                    .call(mapOf("email" to email.value))
                    .await()

                val success = (result.data as Map<*, *>)["success"] as? Boolean ?: false
                if (success) {
                    onSuccess()
                } else {
                    onFailure("Error Sending Code")
                }
            } catch (e: Exception) {
                onFailure(e.message ?: "Unknown Error")
            }
        }
    }

    fun verifyOtp(
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                println(">>> Attempting verifyOtp with email = ${email.value}, code = ${otpCode.value}")

                val result = Firebase.functions
                    .getHttpsCallable("verifyOtp")
                    .call(mapOf(
                        "email" to email.value,
                        "code" to otpCode.value
                    ))
                    .await()

                val success = (result.data as Map<*, *>)["success"] as? Boolean ?: false
                if (success) {
                    println(">>> verifyOtp: success = true")
                    onSuccess()
                } else {
                    println(">>> verifyOtp: success = false")
                    onFailure("Wrong code")
                }
            } catch (e: Exception) {
                println(">>> verifyOtp: exception = ${e.message}")
                onFailure(e.message ?: "Error when checking code")
            }
        }
    }
    fun signInWithGoogle(
        account: GoogleSignInAccount,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            val result = authRepository.signInWithGoogle(account)
            if (result) {
                _isLoggedIn.value = true
                authPreferences.saveIsAuthenticated(true)
                onSuccess()
            } else {
                validationError.value = "Google Sign-In failed"
                onFailure("Google Sign-In failed")
            }
        }
    }

}
