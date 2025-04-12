package com.example.traderapp.data

import com.example.traderapp.data.model.CryptoDto
import com.example.traderapp.data.model.UserData
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

            val user = UserData(
                email,
            )
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

    override suspend fun resetPassword(email: String): Boolean {
        return try {
            auth.sendPasswordResetEmail(email).await()
            true
        } catch (e: Exception) {
            false
        }
    }


    override fun logout() {
        auth.signOut()
    }
}

