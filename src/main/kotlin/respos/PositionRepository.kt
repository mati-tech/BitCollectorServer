package com.mati.respos

import com.mati.dao.PositionDao
import com.mati.dao.UserDao
import com.mati.db.PositionTable
import com.mati.model.Position
import org.jetbrains.exposed.sql.transactions.transaction

class PositionRepository : PositionIntRepos {
    // Create a new Position
    override suspend fun createPosition(position: Position): Position {
        return transaction {
//            val userDao = UserDao.findById(position.userId)
//                ?: throw IllegalArgumentException("User with id ${position.userId} not found")

            val positionDao = PositionDao.new {
                userId = UserDao[position.user_id]
                coin = position.coin
                buyPrice = position.buy_price
                amount = position.amount
                valueUsd = position.value_usd
                hasDetails = position.has_details
                sellPrice = position.sell_price
                positionPnl = position.position_pnl
                buytimestamp = position.buytimestamp
                selltimestamp = position.selltimestamp
            }

            positionDao.toPosition()
        }
    }


    // Get a position by ID
    override suspend fun getPositionById(id: Int): Position? {
        return transaction {
            PositionDao.findById(id)?.toPosition()
        }
    }

    // Get all positions for a user by user ID
    override suspend fun getPositionsByUserId(userId: Int): List<Position> {
        return transaction {
            PositionDao.find { PositionTable.userId eq userId }
                .map { it.toPosition() }
        }
    }

    // Update a position by ID
    override suspend fun updatePosition(id: Int, position: Position): Boolean {
        return transaction {
            val existingPosition = PositionDao.findById(id) ?: return@transaction false
            existingPosition.apply {
                coin = position.coin
                buyPrice = position.buy_price
                amount = position.amount
                valueUsd = position.value_usd
                hasDetails = position.has_details
                sellPrice = position.sell_price
                positionPnl = position.position_pnl
                buytimestamp = position.buytimestamp
                selltimestamp = position.selltimestamp
            }
            true
        }
    }

    // Delete a position by ID
    override suspend fun deletePosition(id: Int): Boolean {
        return transaction {
            val position = PositionDao.findById(id) ?: return@transaction false
            position.delete()
            true
        }
    }

    // Convert PositionDao to Position domain object
    private fun PositionDao.toPosition() = Position(
        id = id.value,
        user_id = userId.id.value,
        coin = coin,
        buy_price = buyPrice,
        amount = amount,
        value_usd = valueUsd,
        has_details = hasDetails,
        sell_price = sellPrice,
        position_pnl = positionPnl,
        buytimestamp = buytimestamp,
        selltimestamp = selltimestamp
    )
}