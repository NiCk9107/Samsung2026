import features.compatibility.configureCompatibilityRouting
import features.login.configureLoginRouting
import features.medicines.configureMedicineRouting
import features.register.configureRegisterRouting
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import plugins.configureRouting
import plugins.configureSerialization
import org.jetbrains.exposed.sql.Database

fun main() {
    Database.connect("jdbc:postgresql://localhost:5432/Samsung", driver = "org.postgresql.Driver",
        user = "postgres", password = "123")
    embeddedServer(CIO, port = 8080, host = "0.0.0.0") {
        configureSerialization()
        configureLoginRouting()
        configureRegisterRouting()
        configureMedicineRouting()
        configureCompatibilityRouting()
        configureRouting()
    }.start(wait = true)
}