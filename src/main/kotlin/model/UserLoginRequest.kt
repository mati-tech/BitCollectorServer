package com.mati.model

@kotlinx.serialization.Serializable
data class UserLoginRequest(
    val email: String,
    val password: String
)
