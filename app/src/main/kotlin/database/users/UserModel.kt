package database.users

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

object UserModel : Table("users") {
    private val login = UserModel.varchar("login", 25)
    private val password = UserModel.varchar("password", 25)
    private val username = UserModel.varchar("username", 30)
    private val email = UserModel.varchar("email", 25)

    fun insert(userDTO: UserDTO) {
        UserModel.insert {
            it[login] = userDTO.login
            it[password] = userDTO.password
            it[username] = userDTO.username
            it[email] = userDTO.email
        }
    }

    fun fetchUser(login: String): UserDTO? {
        val userModel = UserModel.select { UserModel.login eq login }.singleOrNull()
        return userModel?.let {
            UserDTO(
                login = it[UserModel.login],
                password = it[UserModel.password],
                username = it[UserModel.username],
                email = it[UserModel.email],
            )
        }
    }
}