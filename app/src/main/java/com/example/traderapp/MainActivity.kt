package com.example.traderapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.example.traderapp.viewmodel.AuthViewModel
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authViewModel = AuthViewModel()
        firestore = FirebaseFirestore.getInstance()


        authViewModel.onEmailChange("testuser@example.com")
        authViewModel.onPasswordChange("Abcd1234!")
        authViewModel.onConfirmPasswordChange("Abcd1234!")

        authViewModel.register(
            onSuccess = {
                Log.d("FirebaseTest", "✅ Регистрация успешна!")
                testLogin()
            },
            onFailure = { error -> Log.e("FirebaseTest", "❌ Ошибка регистрации: $error") }
        )
    }

    private fun testLogin() {

        authViewModel.login(
            onSuccess = {
                Log.d("FirebaseTest", "✅ Вход успешен!")
                testFirestoreWrite() // После входа пишем данные в Firestore
            },
            onFailure = { error -> Log.e("FirebaseTest", "❌ Ошибка входа: $error") }
        )
    }

    private fun testFirestoreWrite() {

        val userData = hashMapOf(
            "email" to authViewModel.email.value,
            "balance" to 10000,
            "createdAt" to System.currentTimeMillis()
        )

        firestore.collection("users").document(authViewModel.email.value)
            .set(userData)
            .addOnSuccessListener {
                Log.d("FirebaseTest", "Data was written to Firestore!")
                testFirestoreRead() // После записи читаем Firestore
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseTest", "Write error to Firestore: ${e.message}")
            }
    }

    private fun testFirestoreRead() {

        firestore.collection("users").document(authViewModel.email.value)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Log.d("FirebaseTest", "Firestore Data: ${document.data}")
                } else {
                    Log.e("FirebaseTest", "The documents is not found in Firestore")
                }
                testLogout() // После чтения выходим из аккаунта
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseTest", "❌ Ошибка чтения из Firestore: ${e.message}")
            }
    }

    private fun testLogout() {

        authViewModel.logout()
        Log.d("FirebaseTest", "Выход выполнен, пользователь авторизован? ${authViewModel.isAuthenticated.value}")
    }
}
