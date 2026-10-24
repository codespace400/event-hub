package com.example.model

enum class UserRole {
    ORGANIZER,
    ATTENDEE
}

data class UserSession(
    val role: UserRole,
    val name: String,
    val email: String,
    val avatarInitials: String = name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("").uppercase()
)
