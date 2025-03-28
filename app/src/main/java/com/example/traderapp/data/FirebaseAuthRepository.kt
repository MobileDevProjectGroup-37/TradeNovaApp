package com.example.traderapp.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : AuthRepository {

    override fun getIsAuthenticatedFlow(): StateFlow<Boolean> = MutableStateFlow(auth.currentUser != null).asStateFlow()



    override suspend fun register(email: String, password: String): Boolean {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid ?: return false

            val user = UserData(email)
            db.collection("users").document(userId).set(user)
            true
        } catch (e: Exception) {
            false

        }
    }

    override suspend fun login(email: String, password: String): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun logout() {
        auth.signOut()
    }
}

data class UserData(val email: String, val balance: Double = 1000.00, val tradeVolume: Int = 0, val profit: Int = 0)