package com.mati.routing

import com.mati.model.Position
import com.mati.respos.PositionRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.auth.*
import com.mati.utils.JwtConfig
import io.ktor.server.auth.jwt.*
fun Application.configurePositionRouting(repository: PositionRepository) {

        routing {
            authenticate("auth-jwt") {
            route("/positions") {
                // Get all positions for a user by user ID
                get("/byUserId/{userId}") {
                    val userId = call.parameters["userId"]?.toIntOrNull()
                    if (userId == null) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid user ID")
                        return@get
                    }
                    val positions = repository.getPositionsByUserId(userId)
                    if (positions.isEmpty()) {
                        call.respond(HttpStatusCode.NotFound, "No positions found for this user")
                        return@get
                    }
                    call.respond(positions)
                }

                // Get position by ID
                get("/byId/{positionId}") {
                    val positionId = call.parameters["positionId"]?.toIntOrNull()
                    if (positionId == null) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid position ID")
                        return@get
                    }
                    val position = repository.getPositionById(positionId)
                    if (position == null) {
                        call.respond(HttpStatusCode.NotFound, "Position not found")
                        return@get
                    }
                    call.respond(position)
                }

                // Create a new position
                post {
                    try {
                        val position = call.receive<Position>()
                        val createdPosition = repository.createPosition(position)
                        call.respond(HttpStatusCode.Created, createdPosition)
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid position data")
                    }
                }

                // Update position by ID
                put("/{positionId}") {
                    val positionId = call.parameters["positionId"]?.toIntOrNull()
                    if (positionId == null) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid position ID")
                        return@put
                    }
                    val position = call.receive<Position>()
                    val updated = repository.updatePosition(positionId, position)
                    if (updated) {
                        call.respond(HttpStatusCode.OK, "Position updated successfully")
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Position not found")
                    }
                }

                // Delete position by ID
                delete("/{positionId}") {
                    val positionId = call.parameters["positionId"]?.toIntOrNull()
                    if (positionId == null) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid position ID")
                        return@delete
                    }
                    val deleted = repository.deletePosition(positionId)
                    if (deleted) {
                        call.respond(HttpStatusCode.NoContent)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Position not found")
                    }
                }
            }
        }
    }
}
