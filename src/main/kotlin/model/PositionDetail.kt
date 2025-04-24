package com.mati.model

import kotlinx.serialization.Serializable

@Serializable
data class PositionDetail(
    val id: Int,
    val positionId: Int,
    val tradeType: String, // "BUY" or "SELL"
    val tradeAmount: Double,
    val tradePrice: Double,
    val totalCost: Double,
    val availableUsd: Double = 0.0,
    val timestamp: Long
)
