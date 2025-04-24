package com.mati.dao

import com.mati.db.PositionTable
import com.mati.db.UserTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID


class UserDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserDao>(UserTable)

    var email by UserTable.email
    var password by UserTable.password
    var fullName by UserTable.fullName
    var registeredAt by UserTable.registeredAt

    val positions by PositionDao referrersOn PositionTable.userId
}