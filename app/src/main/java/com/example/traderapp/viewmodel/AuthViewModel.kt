package com.example.traderapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    var email = mutableStateOf("")
    var password = mutableStateOf("")
    private var confirmPassword = mutableStateOf("")
    var isAuthenticated = mutableStateOf(false)
    var touchIdEnabled = mutableStateOf(false)
    var validationError = mutableStateOf<String?>(null)

    fun resetFields() {
        email.value = ""
        password.value = ""
        validationError.value = null
    }

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

    fun onTouchIdChange(newValue: Boolean) {
        touchIdEnabled.value = newValue
    }

    fun isEmailValid(): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
        return Pattern.matches(emailPattern, email.value)
    }

    fun isPasswordValid(): Boolean {
        val passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#\$%^&*(),.?\":{}|<>]).{8,}$"
        return Pattern.matches(passwordPattern, password.value)
    }

    private fun arePasswordsMatching(): Boolean {
        return password.value == confirmPassword.value
    }

    private fun validateInputs(): Boolean {
        return isEmailValid() && isPasswordValid()
    }

    fun register(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        if (validateInputs()) {
            auth.createUserWithEmailAndPassword(email.value, password.value)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                        val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
                        val createdAt = System.currentTimeMillis()
                        val user = hashMapOf(
                            "email" to email.value,
                            "balance" to 1000,  // Default Balance
                            "trade_volume" to 0, // Start Trade Value
                            "profit" to 0 // Start profit
                        )

                        db.collection("users").document(userId)
                            .set(user)
                            .addOnSuccessListener {
                                isAuthenticated.value = true
                                onSuccess()
                            }
                            .addOnFailureListener { e ->
                                validationError.value = "Database error: ${e.message}"
                                onFailure(e.message ?: "Database error")
                            }
                    } else {
                        val errorMessage = task.exception?.localizedMessage ?: "Registration failed"
                        validationError.value = errorMessage
                        onFailure(errorMessage)
                    }
                }
        } else {
            validationError.value = when {
                !isEmailValid() -> "Invalid email format"
                !isPasswordValid() -> "Password must be at least 8 characters long, contain a digit, a special character, and both uppercase and lowercase letters."
                !arePasswordsMatching() -> "Passwords do not match"
                else -> "Unknown error"
            }
            onFailure(validationError.value ?: "Unknown error")
        }
    }


    fun login(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email.value, password.value)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()

                    db.collection("users").document(userId).get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                val balance = document.getLong("balance") ?: 0
                                val tradeVolume = document.getLong("trade_volume") ?: 0
                                val profit = document.getLong("profit") ?: 0
                                isAuthenticated.value = true
                                onSuccess()
                            } else {
                                onFailure("User data not found")
                            }
                        }
                        .addOnFailureListener { e ->
                            onFailure("Error fetching user data: ${e.message}")
                        }
                } else {
                    val errorMessage = task.exception?.localizedMessage ?: "Login Error"
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
