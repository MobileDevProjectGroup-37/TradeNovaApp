package com.example.traderapp

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.traderapp.viewmodel.AuthViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class MyFirebaseInstrumentedTest {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var firestore: FirebaseFirestore

    @Before
    fun setUp() {
        authViewModel = AuthViewModel()
        firestore = FirebaseFirestore.getInstance()
    }

    @Test
    fun testFirebaseAuthenticationAndFirestore() = runBlocking {
        // Устанавливаем тестовые данные
        authViewModel.onEmailChange("testuser@example.com")
        authViewModel.onPasswordChange("Abcd1234!")
        authViewModel.onConfirmPasswordChange("Abcd1234!")

        // Register user
        val registerSuccess = registerUser()
        assertTrue("Registration failed", registerSuccess)

        // Login
        val loginSuccess = loginUser()
        assertTrue("Login failed", loginSuccess)

        // Write to Firestore
        val writeSuccess = writeToFirestore()
        assertTrue("Firestore write failed", writeSuccess)

        // Read from Firestore
        val readSuccess = readFromFirestore()
        assertTrue("Firestore read failed", readSuccess)

        // Logout
        authViewModel.logout()
        assertFalse("Logout failed", authViewModel.isAuthenticated.value)
    }

    private suspend fun registerUser(): Boolean {
        return try {
            authViewModel.register(
                onSuccess = { Log.d("FirebaseTest", "Registered successfully!") },
                onFailure = { error -> Log.e("FirebaseTest", "Register Error: $error") }
            )
            true
        } catch (e: Exception) {
            Log.e("FirebaseTest", "Registration Exception: ${e.message}")
            false
        }
    }

    private suspend fun loginUser(): Boolean {
        return try {
            authViewModel.login(
                onSuccess = { Log.d("FirebaseTest", "Login OK!") },
                onFailure = { error -> Log.e("FirebaseTest", "Login Error: $error") }
            )
            true
        } catch (e: Exception) {
            Log.e("FirebaseTest", "Login Exception: ${e.message}")
            false
        }
    }

    private suspend fun writeToFirestore(): Boolean {
        return try {
            val userData = hashMapOf(
                "email" to authViewModel.email.value,
                "balance" to 10000,
                "createdAt" to System.currentTimeMillis()
            )

            firestore.collection("users").document(authViewModel.email.value)
                .set(userData).await()

            Log.d("FirebaseTest", "Data written to Firestore successfully!")
            true
        } catch (e: Exception) {
            Log.e("FirebaseTest", "Firestore Write Exception: ${e.message}")
            false
        }
    }

    private suspend fun readFromFirestore(): Boolean {
        return try {
            val document = firestore.collection("users")
                .document(authViewModel.email.value)
                .get().await()

            if (document.exists()) {
                Log.d("FirebaseTest", "Firestore Data: ${document.data}")
                true
            } else {
                Log.e("FirebaseTest", "Document not found in Firestore")
                false
            }
        } catch (e: Exception) {
            Log.e("FirebaseTest", "Firestore Read Exception: ${e.message}")
            false
        }
    }
}
