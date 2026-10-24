package com.example.model

data class Attendee(
    val id: String,
    val eventId: String,
    val ticketCode: String,
    val name: String,
    val email: String,
    val phone: String = "",
    val ticketType: String = "General Admission",
    val quantity: Int = 1,
    val registeredAt: Long = System.currentTimeMillis(),
    val isCheckedIn: Boolean = false,
    val checkedInAt: Long? = null
)
