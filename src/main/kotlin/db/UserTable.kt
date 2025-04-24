package com.mati.db

import org.jetbrains.exposed.dao.id.IntIdTable

object UserTable : IntIdTable("users") {
    val email = varchar("email", 255).uniqueIndex()
    val password = varchar("password", 255)
    val fullName = varchar("fullName", 255).nullable()
    val registeredAt = long("registeredAt").default(System.currentTimeMillis())
}