package com.mati.dao

import org.jetbrains.exposed.dao.IntEntity
import com.mati.db.PositionDetailTable
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PositionDetailDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PositionDetailDao>(PositionDetailTable)

    var positionId by PositionDao referencedOn PositionDetailTable.positionId
    var tradeType by PositionDetailTable.tradeType
    var tradeAmount by PositionDetailTable.tradeAmount
    var tradePrice by PositionDetailTable.tradePrice
    var totalCost by PositionDetailTable.totalCost
    var availableUsd by PositionDetailTable.availableUsd
    var timestamp by PositionDetailTable.timestamp
}