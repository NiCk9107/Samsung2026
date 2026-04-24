package features.register

import database.tokens.TokenDTO
import database.tokens.TokenModel
import database.users.UserDTO
import database.users.UserModel
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.transactions.transaction
import utils.isEmailValid
import java.util.UUID

class RegisterController(private val call: ApplicationCall) {

    suspend fun registerNewUser() {
        val registerReceiveRemote = call.receive<RegisterReceiveRemote>()

        if (!registerReceiveRemote.email.isEmailValid()) {
            call.respond(HttpStatusCode.BadRequest, "Email is not valid")
            return
        }

        try {
            val token = transaction {
                val existingUser = UserModel.fetchUser(registerReceiveRemote.login)

                if (existingUser != null) {
                    return@transaction null
                }

                val newToken = UUID.randomUUID().toString()

                UserModel.insert(
                    UserDTO(
                        login = registerReceiveRemote.login,
                        password = registerReceiveRemote.password,
                        email = registerReceiveRemote.email,
                        username = ""
                    )
                )

                TokenModel.insert(
                    TokenDTO(
                        rowId = UUID.randomUUID().toString(),
                        login = registerReceiveRemote.login,
                        token = newToken
                    )
                )

                newToken
            }

            if (token == null) {
                call.respond(HttpStatusCode.Conflict, "User already exists")
            } else {
                call.respond(RegisterResponseRemote(token = token))
            }

        } catch (e: ExposedSQLException) {
            call.respond(HttpStatusCode.Conflict, "User already exists")
        }
    }
}