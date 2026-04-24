package features.login

import io.ktor.server.application.Application
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.configureLoginRouting() {
    routing {
        post("/login") {
            val LoginController = LoginController(call)
            LoginController.performLogin()
        }
    }
}
