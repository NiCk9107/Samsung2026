package database.users

import org.jetbrains.exposed.sql.*

object UserModel : Table("users") {
    val id = integer("id").autoIncrement()
    val firstName = varchar("first_name", 100)
    val lastName = varchar("last_name", 100)
    val email = varchar("email", 255)
    val password = varchar("password", 255)
    val phone = varchar("phone", 20).nullable()
    val age = integer("age").nullable()
    val allergies = text("allergies").nullable()

    override val primaryKey = PrimaryKey(id)

    fun fetchUser(login: String): UserDTO? {
        return selectAll()
            .where { email eq login }
            .map {
                UserDTO(
                    id = it[id],
                    firstName = it[firstName],
                    lastName = it[lastName],
                    email = it[email],
                    password = it[password],
                    phone = it[phone],
                    age = it[age],
                    allergies = it[allergies]
                )
            }.singleOrNull()
    }
}