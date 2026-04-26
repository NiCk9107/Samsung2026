package features.register

import database.tokens.TokenDTO
import database.tokens.TokenModel
import database.users.UserModel
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class RegisterController(private val call: ApplicationCall) {

    suspend fun performRegister() {
        val receive = call.receive<RegisterReceiveRemote>()
        val token = UUID.randomUUID().toString()

        val result = transaction {
            val existingUser = UserModel.selectAll()
                .where { UserModel.email eq receive.email }
                .singleOrNull()

            if (existingUser != null) {
                return@transaction null to "User already exists"
            }

            UserModel.insert {
                it[firstName] = ""
                it[lastName] = ""
                it[email] = receive.email
                it[password] = receive.password
            }

            TokenModel.insert(
                TokenDTO(
                    login = receive.email,
                    token = token
                )
            )
            token to null
        }

        val (tokenResult, errorMessage) = result
        when {
            errorMessage != null -> call.respond(HttpStatusCode.BadRequest, errorMessage)
            tokenResult != null -> call.respond(RegisterResponseRemote(token = tokenResult))
        }
    }
}