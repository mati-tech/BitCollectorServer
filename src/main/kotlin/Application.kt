package com.mati

import com.mati.respos.PositionDetailRepos
import com.mati.respos.PositionRepository
import com.mati.respos.UserRepository
import com.mati.routing.configurePositionDetailRouting
import com.mati.routing.configurePositionRouting
import com.mati.routing.configureUserRouting
import com.mati.utils.JwtConfig
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*

fun main(args: Array<String>) {
//    io.ktor.server.netty.EngineMain.main(args)
    val port = System.getenv("PORT")?.toIntOrNull() ?: 8080
    embeddedServer(Netty, port = port, module = Application::module).start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation){
        json()
    }



    val userRepository = UserRepository()
    val positionRepository = PositionRepository()
    val positionDetailRepos = PositionDetailRepos()

    configureSecurity()
    configureUserRouting(userRepository)
    configurePositionRouting(positionRepository)
    configurePositionDetailRouting(positionDetailRepos)
    configureSerialization()
    configureDatabases()
    configureRouting()

}

fun Application.configureSecurity() {
    install(Authentication) {
        jwt("auth-jwt") {
            JwtConfig.configure(this)
        }
    }
}

