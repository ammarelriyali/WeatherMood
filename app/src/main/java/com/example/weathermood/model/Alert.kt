package com.example.weathermood.model

data class Alert(
    val description: String,
    val end: Double,
    val event: String,
    val sender_name: String,
    val start: Double,
    val tags: List<String>
)