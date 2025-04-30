package com.mati.routing

import com.mati.model.User
import com.mati.model.UserLoginRequest
import com.mati.respos.UserIntRepos
import com.mati.utils.JwtConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

fun Application.configureUserRouting(repository: UserIntRepos) {
    routing {
        val passwordEncoder = BCryptPasswordEncoder()
        // Login route outside the /users block
        post("/register") {
            try {
                val user = call.receive<User>()

                // Check if user already exists by email
                if (repository.userExistsByEmail(user.email)) {
                    call.respond(HttpStatusCode.Conflict, "User already exists")
                    return@post
                }

                // Hash the password
                val hashedPassword = passwordEncoder.encode(user.password)

                // Create a new user object with the hashed password
                val newUser = user.copy(password = hashedPassword, role = "user") // Set role to "user"
                repository.createUser(newUser)

                call.respond(HttpStatusCode.Created, "Registered successfully")
            } catch (e: Exception) {
                println("Registration error: ${e.message}")
                call.respond(HttpStatusCode.BadRequest, "Invalid registration data")
            }
        }

        // Login route
        post("/login") {
            try {
                val request = call.receive<UserLoginRequest>()
                val user = repository.getUserByEmail(request.email)

                if (user != null) {
                    // Verify the entered password against the stored hashed password
                    val isPasswordValid = passwordEncoder.matches(request.password, user.password)

                    if (isPasswordValid) {
                        val token = JwtConfig.generateToken(user.email, user.role)
                        call.respond(mapOf("token" to token))
                    } else {
                        call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
                    }
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
                }
            } catch (e: Exception) {
                println("Login error: ${e.message}")
                call.respond(HttpStatusCode.BadRequest, "Invalid login request")
            }
        }

        authenticate("auth-jwt") {
            route("/users") {
                // Get all users
                get {
                    val principal = call.principal<JWTPrincipal>()
                    val role = principal!!.payload.getClaim("role").asString()

                    if (role != "admin") {
                        call.respond(HttpStatusCode.Forbidden, "Only admin can access this.")
                        return@get
                    }

                    val users = repository.getAllUsers()
                    call.respond(users)
                }

                // Get user by ID
                get("/byId/{userId}") {
                    val userId = call.parameters["userId"]?.toIntOrNull()
                    if (userId == null) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid user ID")
                        return@get
                    }
                    val user = repository.getUserById(userId)
                    if (user == null) {
                        call.respond(HttpStatusCode.NotFound, "User not found")
                        return@get
                    }
                    call.respond(user)
                }

                // Get user by email
                get("/byEmail/{email}") {
                    val email = call.parameters["email"]
                    if (email.isNullOrBlank()) {
                        call.respond(HttpStatusCode.BadRequest, "Email is required")
                        return@get
                    }
                    val user = repository.getUserByEmail(email)
                    if (user == null) {
                        call.respond(HttpStatusCode.NotFound, "User not found")
                        return@get
                    }
                    call.respond(user)
                }

    //             Create a new user
//                post {
//                    try {
//                        val user = call.receive<User>()
//                        // Check if email already exists
//                        if (repository.userExistsByEmail(user.email)) {
//                            call.respond(HttpStatusCode.Conflict, "User with this email already exists")
//                            return@post
//                        }
//                        repository.createUser(user)
//                        call.respond(HttpStatusCode.Created, user)
//                    } catch (e: Exception) {
//                        println("Error: ${e.message}")
//                        call.respond(HttpStatusCode.BadRequest, "Invalid user data")
//                    }
//                }

    //            post {
    //                try {
    //                    val user = call.receive<User>()
    //                    repository.createUser(user)
    //                    call.respond(HttpStatusCode.Created)
    //                } catch (e: Exception) {
    //                    call.respond(HttpStatusCode.BadRequest, "Invalid user data")
    //                }
    //            }

                // Update an existing user
                put("/{userId}") {
                    val userId = call.parameters["userId"]?.toIntOrNull()
                    if (userId == null) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid user ID")
                        return@put
                    }
                    val user = call.receive<User>()
                    val updated = repository.updateUser(userId, user)
                    if (updated) {
                        call.respond(HttpStatusCode.OK, "User updated successfully")
                    } else {
                        call.respond(HttpStatusCode.NotFound, "User not found")
                    }
                }

                // Delete a user by ID
                delete("/{userId}") {
                    val principal = call.principal<JWTPrincipal>()
                    val role = principal!!.payload.getClaim("role").asString()

                    if (role != "admin") {
                        call.respond(HttpStatusCode.Forbidden, "Only admin can delete users.")
                        return@delete
                    }

                    val userId = call.parameters["userId"]?.toIntOrNull()
                    if (userId == null) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid user ID")
                        return@delete
                    }
                    val deleted = repository.deleteUser(userId)
                    if (deleted) {
                        call.respond(HttpStatusCode.NoContent)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "User not found")
                    }
                }
            }
        }
    }
}
