package com.mati.model

import com.mati.utils.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@kotlinx.serialization.Serializable
data class User(
    val id: Int,
    val username: String,
    val email: String,
    val passwordHash: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime? = null
)
