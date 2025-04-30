package com.mati.respos

import com.mati.dao.PositionDao
import com.mati.dao.PositionDetailDao
import com.mati.db.PositionDetailTable
import com.mati.model.PositionDetail
import org.jetbrains.exposed.sql.transactions.transaction

class PositionDetailRepos : PositionDetailIntRepos {
    override suspend fun createPositionDetail(detail: PositionDetail): PositionDetail {
        return transaction {
            val dao = PositionDetailDao.new {
                positionId = PositionDao[detail.positionId]
                tradeType = detail.tradeType
                tradeAmount = detail.tradeAmount
                tradePrice = detail.tradePrice
                totalCost = detail.totalCost
                availableUsd = detail.availableUsd
                timestamp = detail.timestamp
            }
            dao.toDetail()
        }
    }

    override suspend fun getDetailById(id: Int): PositionDetail? {
        return transaction {
            PositionDetailDao.findById(id)?.toDetail()
        }
    }

    override suspend fun getDetailsByPositionId(positionId: Int): List<PositionDetail> {
        return transaction {
            PositionDetailDao.find { PositionDetailTable.positionId eq positionId }
                .map { it.toDetail() }
        }
    }

    override suspend fun updatePositionDetail(id: Int, detail: PositionDetail): Boolean {
        return transaction {
            val dao = PositionDetailDao.findById(id) ?: return@transaction false
            dao.tradeType = detail.tradeType
            dao.tradeAmount = detail.tradeAmount
            dao.tradePrice = detail.tradePrice
            dao.totalCost = detail.totalCost
            dao.availableUsd = detail.availableUsd
            dao.timestamp = detail.timestamp
            true
        }
    }

    override suspend fun deletePositionDetail(id: Int): Boolean {
        return transaction {
            val dao = PositionDetailDao.findById(id) ?: return@transaction false
            dao.delete()
            true
        }
    }
    fun PositionDetailDao.toDetail() = PositionDetail(
        id = id.value,
        positionId = positionId.id.value,
        tradeType = tradeType,
        tradeAmount = tradeAmount,
        tradePrice = tradePrice,
        totalCost = totalCost,
        availableUsd = availableUsd,
        timestamp = timestamp
    )
}