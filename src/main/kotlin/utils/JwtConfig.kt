package com.mati.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.auth.jwt.*
import java.util.*

object JwtConfig {
    private const val secret = "my_secret_key_123" // use .env in real projects
    private const val issuer = "com.mati"
    private const val validityInMs = 36_000_00 * 10 // 10 hours
//    private const val validityInMs = 60 // 10 hours

    private val algorithm = Algorithm.HMAC256(secret)

    fun generateToken(email: String, role: String): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withClaim("email", email)
        .withClaim("role", role)
        .withExpiresAt(Date(System.currentTimeMillis() + validityInMs))
        .sign(algorithm)

    fun configure(config: JWTAuthenticationProvider.Config) {
        config.verifier(JWT.require(algorithm).withIssuer(issuer).build())
        config.validate { credential ->
            val email = credential.payload.getClaim("email").asString()
            if (email != null) JWTPrincipal(credential.payload) else null
        }
    }
}
