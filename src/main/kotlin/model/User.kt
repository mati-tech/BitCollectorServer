package com.mati.model

import com.mati.utils.LocalDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val email: String,
    val password: String,
    val full_name: String,
    val role: String = "user",
    val registered_at: Long = System.currentTimeMillis()
)
