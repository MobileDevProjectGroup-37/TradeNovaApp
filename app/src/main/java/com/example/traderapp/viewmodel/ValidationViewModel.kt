package com.example.traderapp.viewmodel


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.util.regex.Pattern

class ValidationViewModel : ViewModel() {

    var validationError = mutableStateOf<String?>(null)

    // Валидация email
    fun isEmailValid(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
        return Pattern.matches(emailPattern, email)
    }

    // Валидация пароля
    fun isPasswordValid(password: String): Boolean {
        val passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#\$%^&*(),.?\":{}|<>]).{8,}$"
        return Pattern.matches(passwordPattern, password)
    }

    // Проверка на совпадение паролей
    fun arePasswordsMatching(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }

    // Валидация всех входных данных
    fun validateInputs(email: String, password: String, confirmPassword: String): Boolean {
        return isEmailValid(email) && isPasswordValid(password)
    }

    // Метод для обработки ошибок валидации
    fun getValidationError(email: String, password: String, confirmPassword: String): String {
        return when {
            !isEmailValid(email) -> "Invalid email format"
            !isPasswordValid(password) -> "Password must be at least 8 characters long, contain a digit, a special character, and both uppercase and lowercase letters."
            !arePasswordsMatching(password, confirmPassword) -> "Passwords do not match"
            else -> "Unknown error"
        }
    }
}
