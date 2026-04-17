import features.login.configureLoginRouting
import features.register.configureRegisterRouting
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import plugins.configureRouting
import plugins.configureSerialization

fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0") {
        configureSerialization()
        configureLoginRouting()
        configureRegisterRouting()
        configureRouting()
    }.start(wait = true)
}