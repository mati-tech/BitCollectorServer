package com.mati.respos

import com.mati.model.Position

interface PositionIntRepos {

    // Create a new Position
    suspend fun createPosition(position: Position): Position

    // Get a position by ID
    suspend fun getPositionById(id: Int): Position?

    // Get all positions for a user by user ID
    suspend fun getPositionsByUserId(userId: Int): List<Position>

    // Update a position by ID
    suspend fun updatePosition(id: Int, position: Position): Boolean

    // Delete a position by ID
    suspend fun deletePosition(id: Int): Boolean

    // Check if a position exists by userId and coin

}