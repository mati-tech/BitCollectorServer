package com.mati.db

import org.jetbrains.exposed.dao.id.IntIdTable

object UserTable : IntIdTable("users") {
    val email = varchar("email", 255).uniqueIndex()
    val password = varchar("password", 255)
    val fullName = varchar("full_name", 255)
    val role = varchar("role", 50).default("user")
    val registeredAt = long("registered_at").default(System.currentTimeMillis())
}