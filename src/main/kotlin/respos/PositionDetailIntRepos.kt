package com.mati.respos

import com.mati.model.PositionDetail

interface PositionDetailIntRepos {

    // Create a new position detail
    suspend fun createPositionDetail(detail: PositionDetail): PositionDetail

    // Get a detail by its ID
    suspend fun getDetailById(id: Int): PositionDetail?

    // Get all details for a specific position ID
    suspend fun getDetailsByPositionId(positionId: Int): List<PositionDetail>

    // Update a position detail
    suspend fun updatePositionDetail(id: Int, detail: PositionDetail): Boolean

    // Delete a position detail by ID
    suspend fun deletePositionDetail(id: Int): Boolean
}
