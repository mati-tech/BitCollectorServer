package com.mati.routing

import com.mati.model.PositionDetail
import com.mati.model.User
import com.mati.respos.PositionDetailRepos
import com.mati.respos.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import kotlin.text.get
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.response.*

fun Application.configurePositionDetailRouting(repository: PositionDetailRepos) {
    routing {
        authenticate("auth-jwt") {
            route("/positionDetails") {
                // Get all position details by position ID
                get("/byPositionId/{positionId}") {
                    val positionId = call.parameters["positionId"]?.toIntOrNull()
                    if (positionId == null) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid position ID")
                        return@get
                    }
                    val positionDetails = repository.getDetailsByPositionId(positionId)
                    if (positionDetails.isEmpty()) {
                        call.respond(HttpStatusCode.NotFound, "No position details found for this position")
                        return@get
                    }
                    call.respond(positionDetails)
                }

                // Get position detail by ID
                get("/byId/{detailId}") {
                    val detailId = call.parameters["detailId"]?.toIntOrNull()
                    if (detailId == null) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid position detail ID")
                        return@get
                    }
                    val positionDetail = repository.getDetailById(detailId)
                    if (positionDetail == null) {
                        call.respond(HttpStatusCode.NotFound, "Position detail not found")
                        return@get
                    }
                    call.respond(positionDetail)
                }

                // Create a new position detail
                post {
                    try {
                        val positionDetail = call.receive<PositionDetail>()
                        val createdDetail = repository.createPositionDetail(positionDetail)
                        call.respond(HttpStatusCode.Created, createdDetail)
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid position detail data")
                    }
                }

                // Update position detail by ID
                put("/{detailId}") {
                    val detailId = call.parameters["detailId"]?.toIntOrNull()
                    if (detailId == null) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid position detail ID")
                        return@put
                    }
                    val positionDetail = call.receive<PositionDetail>()
                    val updated = repository.updatePositionDetail(detailId, positionDetail)
                    if (updated) {
                        call.respond(HttpStatusCode.OK, "Position detail updated successfully")
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Position detail not found")
                    }
                }

                // Delete position detail by ID
                delete("/{detailId}") {
                    val detailId = call.parameters["detailId"]?.toIntOrNull()
                    if (detailId == null) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid position detail ID")
                        return@delete
                    }
                    val deleted = repository.deletePositionDetail(detailId)
                    if (deleted) {
                        call.respond(HttpStatusCode.NoContent)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Position detail not found")
                    }
                }
            }
        }
    }
}
