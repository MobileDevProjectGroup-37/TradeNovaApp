package com.example.traderapp.data.network

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.traderapp.data.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSession @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> = _userData.asStateFlow()

    fun updateUser(user: UserData) {
        _userData.value = user
    }

    fun clear() {
        _userData.value = null
    }

    suspend fun loadUserData() {
        val uid = auth.currentUser?.uid ?: return
        try {
            val snapshot = db.collection("users").document(uid).get().await()
            val user = snapshot.toObject(UserData::class.java)
            Log.d("\"FIREBASE RAW\"", snapshot.data.toString())
            user?.let { updateUser(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
@HiltViewModel
class UserSessionViewModel @Inject constructor(
    val userSession: UserSession
) : ViewModel()