package com.mati.respos

import com.mati.dao.PositionDao
import com.mati.model.User
import com.mati.dao.UserDao
import com.mati.model.Position
import org.jetbrains.exposed.sql.transactions.transaction
//class UserRepository : UserIntRepos {
//
//    // Create a new User
//    override suspend fun createUser(user: User): User {
//        return transaction {
//            UserDao.new {
//                username = user.username
//                email = user.email
//                password = user.password
//                phone = user.phone
//                positions = user.positions.map { position ->
//                    PositionDao.new {
//                        coin = position.coin
//                        buyPrice = position.buyPrice
//                        amount = position.amount
//                        valueUsd = position.valueUsd
//                        sellPrice = position.sellPrice
//                        positionPnl = position.positionPnl
//                        buytimestamp = position.buytimestamp
//                        selltimestamp = position.selltimestamp
//                    }
//                }
//            }.toUser()
//        }
//    }
//
//    // Get a user by ID
//    override suspend fun getUserById(id: Int): User? {
//        return transaction {
//            UserDao.findById(id)?.toUser()
//        }
//    }
//
//    // Get a user by email
//    override suspend fun getUserByEmail(email: String): User? {
//        return transaction {
//            UserDao.find { UsersTable.email eq email }.singleOrNull()?.toUser()
//        }
//    }
//
//    // Get all users
//    override suspend fun getAllUsers(): List<User> {
//        return transaction {
//            UserDao.all().map { it.toUser() }
//        }
//    }
//
//    // Update a user
//    override suspend fun updateUser(id: Int, user: User): Boolean {
//        return transaction {
//            val existingUser = UserDao.findById(id) ?: return@transaction false
//            existingUser.apply {
//                name = user.name
//                email = user.email
//                password = user.password
//                address = user.address
//                phone = user.phone
//                // Update positions if needed (This can be further improved)
//                positions = user.positions.map { position ->
//                    PositionDao.new {
//                        coin = position.coin
//                        buyPrice = position.buyPrice
//                        amount = position.amount
//                        valueUsd = position.valueUsd
//                        sellPrice = position.sellPrice
//                        positionPnl = position.positionPnl
//                        buytimestamp = position.buytimestamp
//                        selltimestamp = position.selltimestamp
//                    }
//                }
//            }
//            true
//        }
//    }
//
//    // Delete a user by ID
//    override suspend fun deleteUser(id: Int): Boolean {
//        return transaction {
//            val user = UserDao.findById(id) ?: return@transaction false
//            user.delete()
//            true
//        }
//    }
//
//    // Check if a user exists by email
//    override suspend fun userExistsByEmail(email: String): Boolean {
//        return transaction {
//            UserDao.find { UsersTable.email eq email }.count() > 0
//        }
//    }
//
//    private fun UserDao.toUser() = User(
//        id = id.value,
//        name = name,
//        email = email,
//        password = password,
//        address = address,
//        phone = phone,
//        positions = positions.map { it.toPosition() }  // Convert positions to Position objects
//    )
//
//    // Convert PositionDao to Position (for the User model)
//    private fun PositionDao.toPosition() = Position(
//        id = id.value,
//        coin = coin,
//        buyPrice = buyPrice,
//        amount = amount,
//        valueUsd = valueUsd,
//        sellPrice = sellPrice,
//        positionPnl = positionPnl,
//        buytimestamp = buytimestamp,
//        selltimestamp = selltimestamp
//    )
//}

package com.mati.repositories

import com.mati.dao.PositionDao
import com.mati.dao.UserDao
import com.mati.db.UserTable
import com.mati.model.User
import com.mati.model.Position
import org.jetbrains.exposed.sql.transactions.transaction

class UserRepository : UserIntRepos {

    // Create a new User
    override suspend fun createUser(user: User): User {
        return transaction {
            // Create the User and its associated Positions
            UserDao.new {
                email = user.email
                password = user.password
                fullName = user.fullName
                registeredAt = user.registeredAt ?: System.currentTimeMillis()

                // Create associated positions for the user
                positions = user.positions.map { position ->
                    PositionDao.new {
                        coin = position.coin
                        buyPrice = position.buyPrice
                        amount = position.amount
                        valueUsd = position.valueUsd
                        sellPrice = position.sellPrice
                        positionPnl = position.positionPnl
                        buytimestamp = position.buytimestamp
                        selltimestamp = position.selltimestamp
                    }
                }
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
                fullName = user.fullName
                // Update the registeredAt timestamp if needed
                registeredAt = user.registeredAt ?: registeredAt

                // Update positions if necessary (This can be optimized further)
                positions = user.positions.map { position ->
                    PositionDao.new {
                        coin = position.coin
                        buyPrice = position.buyPrice
                        amount = position.amount
                        valueUsd = position.valueUsd
                        sellPrice = position.sellPrice
                        positionPnl = position.positionPnl
                        buytimestamp = position.buytimestamp
                        selltimestamp = position.selltimestamp
                    }
                }
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

    // Convert UserDao to User domain object
    private fun UserDao.toUser() = User(
        id = id.value,
        email = email,
        password = password,
        fullName = fullName,
        registeredAt = registeredAt,
        positions = positions.map { it.toPosition() }  // Map the PositionDao to Position objects
    )

    // Convert PositionDao to Position domain object
    private fun PositionDao.toPosition() = Position(
        id = id.value,
        coin = coin,
        buyPrice = buyPrice,
        amount = amount,
        valueUsd = valueUsd,
        sellPrice = sellPrice,
        positionPnl = positionPnl,
        buytimestamp = buytimestamp,
        selltimestamp = selltimestamp
    )
}



