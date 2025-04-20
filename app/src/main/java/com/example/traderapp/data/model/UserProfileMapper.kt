package com.example.traderapp.data.model

import com.example.traderapp.R

fun UserData.toUserProfile(): UserProfile {
    return UserProfile(
        profileImageRes = R.drawable.profile_icon,
        userName = this.email.substringBefore("@"),
        userEmail = this.email,
        userId = "ID-${this.email.hashCode()}",
        isVerified = this.isVerified()
    )
}


fun UserData.isVerified(): Boolean {
    return this.email.endsWith("@gmail.com") || this.profit > 100
}
