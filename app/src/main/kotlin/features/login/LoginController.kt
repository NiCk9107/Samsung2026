package features.login

import database.tokens.TokenDTO
import database.tokens.TokenModel
import database.users.UserModel
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class LoginController(private val call: ApplicationCall) {

    suspend fun performLogin() {
        val receive = call.receive<LoginReceiveRemote>()

        val result = transaction {
            val userDTO = UserModel.fetchUser(receive.login)

            if (userDTO == null) {
                return@transaction null to "User not found"
            }

            if (userDTO.password != receive.password) {
                return@transaction null to "Invalid password"
            }

            val token = UUID.randomUUID().toString()

            TokenModel.insert(
                TokenDTO(
                    login = receive.login,
                    token = token
                )
            )
            token to null
        }
        val (token, errorMessage) = result
        when {
            errorMessage != null -> call.respond(HttpStatusCode.BadRequest, errorMessage)
            token != null -> call.respond(LoginResponseRemote(token = token))
        }
    }
}