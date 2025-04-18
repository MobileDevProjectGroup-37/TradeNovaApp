package com.example.traderapp.data.model



data class UserProfile(
    val profileImageRes: Int,
    val userName: String,
    val userEmail: String,
    val userId: String,
    val isVerified: Boolean
)
