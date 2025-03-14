package com.example.traderapp.viewmodel


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()


    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var confirmPassword = mutableStateOf("")
    var isAuthenticated = mutableStateOf(false)


    var validationError = mutableStateOf<String?>(null)


    init {
        checkUserAuthenticationStatus()
    }

    private fun checkUserAuthenticationStatus() {
        isAuthenticated.value = auth.currentUser != null
    }


    fun onEmailChange(newEmail: String) {
        email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        password.value = newPassword
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        confirmPassword.value = newConfirmPassword
    }


    private fun isEmailValid(): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
        return Pattern.matches(emailPattern, email.value)
    }


    private fun isPasswordValid(): Boolean {
        val passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#\$%^&*(),.?\":{}|<>]).{8,}$"
        return Pattern.matches(passwordPattern, password.value)
    }


    private fun arePasswordsMatching(): Boolean {
        return password.value == confirmPassword.value
    }


    fun validateInputs(): Boolean {
        return isEmailValid() && isPasswordValid() && arePasswordsMatching()
    }

    fun register(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        if (validateInputs()) {
            auth.createUserWithEmailAndPassword(email.value, password.value)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        isAuthenticated.value = true
                        onSuccess()
                    } else {
                        val errorMessage = task.exception?.localizedMessage ?: "Ошибка регистрации"
                        validationError.value = errorMessage
                        onFailure(errorMessage)
                    }
                }
        } else {
            validationError.value = when {
                !isEmailValid() -> "Некорректный формат email"
                !isPasswordValid() -> "Пароль должен содержать 8+ символов, цифры, буквы и спецсимволы"
                !arePasswordsMatching() -> "Пароли не совпадают"
                else -> "Неизвестная ошибка"
            }
            onFailure(validationError.value ?: "Ошибка валидации")
        }
    }


    fun login(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email.value, password.value)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    isAuthenticated.value = true
                    onSuccess()
                } else {
                    val errorMessage = task.exception?.localizedMessage ?: "Ошибка входа"
                    validationError.value = errorMessage
                    onFailure(errorMessage)
                }
            }
    }


    fun logout() {
        auth.signOut()
        isAuthenticated.value = false
    }
}
