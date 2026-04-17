package features.login

import cache.InMemoryCache
import cache.TokenCache
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import java.util.UUID

fun Application.configureLoginRouting() {
    routing {
        post("/login") {
            val recieve = call.receive(LoginReceiveRemote::class)
            val first = InMemoryCache.UserList.firstOrNull { it.login == recieve.login }
            if (first == null){
                call.respond(HttpStatusCode.BadRequest, "User not found")
            } else {
                if (first.password == recieve.password){
                    val token = UUID.randomUUID().toString()
                    InMemoryCache.token.add(TokenCache(login = recieve.login, token = token))
                    call.respond(LoginResponseRemote(token = token))
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid password")
                }
            }
        }
    }
}
