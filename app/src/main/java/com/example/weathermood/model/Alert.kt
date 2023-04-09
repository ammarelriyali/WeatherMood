package com.example.weathermood.model

data class AlertResponse(
val alerts :List<Alert>,
val current: Current
)
data class Alert(
    val description: String,
    val end: Int,
    val event: String,
    val sender_name: String,
    val start: Int,
    val tags: List<String>
):java.io.Serializable