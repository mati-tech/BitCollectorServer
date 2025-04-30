package com.mati.dao

import com.mati.db.PositionDetailTable
import com.mati.db.PositionTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PositionDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PositionDao>(PositionTable)

    var userId by UserDao referencedOn PositionTable.userId
    var coin by PositionTable.coin
    var buyPrice by PositionTable.buyPrice
    var amount by PositionTable.amount
    var valueUsd by PositionTable.valueUsd
    var hasDetails by PositionTable.hasDetails
    var sellPrice by PositionTable.sellPrice
    var positionPnl by PositionTable.positionPnl
    var buytimestamp by PositionTable.buytimestamp
    var selltimestamp by PositionTable.selltimestamp

//    val details by PositionDetailDao referrersOn PositionDetailTable.positionId
}