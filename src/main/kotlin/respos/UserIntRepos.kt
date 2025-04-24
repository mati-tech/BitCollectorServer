package com.mati.respos

import com.mati.model.User

// Define the repository interface for the User entity
interface UserIntRepos {

    // Create a new User
    suspend fun createUser(user: User): User

    // Get a user by ID
    suspend fun getUserById(id: Int): User?

    // Get a user by email
    suspend fun getUserByEmail(email: String): User?

    // Get all users
    suspend fun getAllUsers(): List<User>

    // Update a user
    suspend fun updateUser(id: Int, user: User): Boolean

    // Delete a user by ID
    suspend fun deleteUser(id: Int): Boolean

    // Check if a user exists by email
    suspend fun userExistsByEmail(email: String): Boolean
}
