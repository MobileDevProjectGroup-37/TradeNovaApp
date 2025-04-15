package com.example.traderapp.data

import com.example.traderapp.data.model.CryptoDto
import com.example.traderapp.data.model.UserData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
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

    override fun logout() {
        auth.signOut()
    }
    override suspend fun signInWithGoogle(account: GoogleSignInAccount): Boolean {
        return try {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            val result = auth.signInWithCredential(credential).await()
            val userId = result.user?.uid ?: return false

            val userRef = db.collection("users").document(userId)


            val snapshot = userRef.get().await()
            if (!snapshot.exists()) {

                val newUser = UserData(email = result.user?.email ?: "no-email")
                userRef.set(newUser).await()
            } else {

                val email = result.user?.email ?: "no-email"
                userRef.update("email", email).await()

            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}

