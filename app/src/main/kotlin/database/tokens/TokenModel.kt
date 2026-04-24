package database.tokens

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

object TokenModel: Table("tokens") {
    private val id = TokenModel.varchar("id",50)
    private val login = TokenModel.varchar("login", 50)
    private val token = TokenModel.varchar("token", 75)

    fun insert(tokenDTO: TokenDTO){
        transaction {
            TokenModel.insert {
                it[id] = tokenDTO.rowId
                it[login] = tokenDTO.login
                it[token] = tokenDTO.token
            }
        }
    }
}