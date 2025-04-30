package com.mati.model

import kotlinx.serialization.Serializable

@Serializable
data class Position(
    val id: Int,
    val user_id: Int,
    val coin: String,
    val buy_price: Double,
    val amount: Double,
    val value_usd: Double,
    val has_details: Boolean = false,
    val sell_price: Double? = null,
    val position_pnl: Double = 0.0,
    val buytimestamp: Long,
    val selltimestamp: Long? = null,

)
