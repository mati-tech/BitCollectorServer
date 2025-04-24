package com.mati.db

import org.jetbrains.exposed.dao.id.IntIdTable

object PositionDetailTable : IntIdTable("position_details") {
    val positionId = reference("positionId", PositionTable)
    val tradeType = varchar("tradeType", 10) // "BUY" or "SELL"
    val tradeAmount = double("tradeAmount")
    val tradePrice = double("tradePrice")
    val totalCost = double("totalCost")
    val availableUsd = double("availableUsd").default(0.0)
    val timestamp = long("timestamp").default(System.currentTimeMillis())
}