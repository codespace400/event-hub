package com.example.model

data class Event(
    val id: String,
    val name: String,
    val description: String,
    val date: String,
    val time: String,
    val venue: String,
    val coverImage: String, // Resource name like "cover_tech_summit" or custom identifier
    val capacity: Int,
    val registeredCount: Int = 0,
    val price: String = "Free",
    val category: String = "General",
    val isPublished: Boolean = true,
    val organizerName: String = "EventHub Productions",
    val createdAt: Long = System.currentTimeMillis()
)
