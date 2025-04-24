package com.mati.model

import kotlinx.serialization.Serializable

@Serializable
data class Position(
    val id: Int,
    val coin: String,
    val buyPrice: Double,
    val amount: Double,
    val valueUsd: Double,
    val hasDetails: Boolean = false,
    val sellPrice: Double? = null,
    val positionPnl: Double = 0.0,
    val buyTimestamp: Long,
    val sellTimestamp: Long? = null,
    val userId: Int? = null
)
