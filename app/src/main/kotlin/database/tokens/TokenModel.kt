package database.tokens

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object TokenModel : Table("tokens") {
    val id = integer("id").autoIncrement()
    val login = varchar("login", 255)
    val token = varchar("token", 255)

    override val primaryKey = PrimaryKey(id)

    fun insert(tokenDTO: TokenDTO) {
        TokenModel.insert {
            it[login] = tokenDTO.login
            it[token] = tokenDTO.token
        }
    }
}