package com.mati.db

import org.jetbrains.exposed.dao.id.IntIdTable

object PositionTable : IntIdTable("positions") {
    val userId = reference("user_id", UserTable)
    val coin = varchar("coin", 50)
    val buyPrice = double("buy_price")
    val amount = double("amount")
    val valueUsd = double("value_usd")
    val hasDetails = bool("has_details").default(false)
    val sellPrice = double("sell_price").nullable()
    val positionPnl = double("position_pnl").default(0.0)
    val buytimestamp = long("buytimestamp").default(System.currentTimeMillis())
    val selltimestamp = long("selltimestamp").nullable()
}