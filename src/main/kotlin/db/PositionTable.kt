package com.mati.db

import org.jetbrains.exposed.dao.id.IntIdTable

object PositionTable : IntIdTable("positions") {
    val userId = reference("userId", UserTable)
    val coin = varchar("coin", 50)
    val buyPrice = double("buyPrice")
    val amount = double("amount")
    val valueUsd = double("valueUsd")
    val hasDetails = bool("hasDetails").default(false)
    val sellPrice = double("sellPrice").nullable()
    val positionPnl = double("positionPnl").default(0.0)
    val buytimestamp = long("buytimestamp").default(System.currentTimeMillis())
    val selltimestamp = long("selltimestamp").nullable()
}