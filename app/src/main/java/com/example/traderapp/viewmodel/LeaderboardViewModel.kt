package com.example.traderapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.traderapp.data.model.UserData
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val db: FirebaseFirestore
) : ViewModel() {

    private val _leaderboard = MutableStateFlow<List<UserData>>(emptyList())
    val leaderboard: StateFlow<List<UserData>> = _leaderboard.asStateFlow()

    init {
        loadLeaderboard()
    }

    fun loadLeaderboard() {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("users")

                    .orderBy("profit", com.google.firebase.firestore.Query.Direction.DESCENDING)
                    .limit(20)
                    .get()
                    .await()

                val users = snapshot.documents.mapNotNull { it.toObject(UserData::class.java) }

                _leaderboard.value = users
            } catch (e: Exception) {
                Log.e("LEADERBOARD", "Failed to load leaderboard: ${e.message}")
            }
        }
    }
}
