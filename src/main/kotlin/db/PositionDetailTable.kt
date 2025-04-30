package com.mati.db

import org.jetbrains.exposed.dao.id.IntIdTable

object PositionDetailTable : IntIdTable("position_details") {
    val positionId = reference("position_id", PositionTable)
    val tradeType = varchar("trade_type", 10) // "BUY" or "SELL"
    val tradeAmount = double("trade_amount")
    val tradePrice = double("trade_price")
    val totalCost = double("total_cost")
    val availableUsd = double("available_usd").default(0.0)
    val timestamp = long("timestamp").default(System.currentTimeMillis())
}