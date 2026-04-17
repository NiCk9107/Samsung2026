package features.register

import cache.InMemoryCache
import cache.TokenCache
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import utils.isEmailValid
import java.util.UUID

fun Application.configureRegisterRouting() {
    routing {
        post("/register") {
            val recieve = call.receive(RegisterReceiveRemote::class)
            if (!recieve.email.isEmailValid()) {
                call.respond(HttpStatusCode.BadRequest, "Invalid email format")
            }
            if (InMemoryCache.UserList.map { it.login }.contains(recieve.login))   {
                call.respond(HttpStatusCode.Conflict, "User not registered")
            }
            val token = UUID.randomUUID().toString()
            InMemoryCache.UserList.add(recieve)
            InMemoryCache.token.add(TokenCache(login = recieve.login, token = token))
            call.respond(RegisterResponseRemote(token = token))
        }
    }
}
