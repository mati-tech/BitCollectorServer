package com.mati.respos

import com.mati.model.User
import com.mati.dao.UserDao
import org.jetbrains.exposed.sql.transactions.transaction
import com.mati.db.UserTable

class UserRepository : UserIntRepos {

    // Create a new User
    override suspend fun createUser(user: User): User {
        return transaction {
            // Create the User and its associated Positions
            UserDao.new {
                email = user.email
                password = user.password
                fullName = user.full_name
            }.toUser()
        }
    }

    // Get a user by ID
    override suspend fun getUserById(id: Int): User? {
        return transaction {
            UserDao.findById(id)?.toUser()
        }
    }

    // Get a user by email
    override suspend fun getUserByEmail(email: String): User? {
        return transaction {
            UserDao.find { UserTable.email eq email }.singleOrNull()?.toUser()
        }
    }

    // Get all users
    override suspend fun getAllUsers(): List<User> {
        return transaction {
            UserDao.all().map { it.toUser() }
        }
    }

    // Update a user
    override suspend fun updateUser(id: Int, user: User): Boolean {
        return transaction {
            val existingUser = UserDao.findById(id) ?: return@transaction false
            existingUser.apply {
                email = user.email
                password = user.password
                fullName = user.full_name
            }
            true
        }
    }

    // Delete a user by ID
    override suspend fun deleteUser(id: Int): Boolean {
        return transaction {
            val user = UserDao.findById(id) ?: return@transaction false
            user.delete()
            true
        }
    }

    // Check if a user exists by email
    override suspend fun userExistsByEmail(email: String): Boolean {
        return transaction {
            UserDao.find { UserTable.email eq email }.count() > 0
        }
    }

    private fun UserDao.toUser() = User(
        id = this.id.value,
        email = email,
        password = password,
        full_name = fullName,
    )
}



