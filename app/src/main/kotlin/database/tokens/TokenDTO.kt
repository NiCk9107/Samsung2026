package database.tokens

import kotlinx.serialization.Serializable

@Serializable
data class TokenDTO(
    val id: Int = 0,
    val login: String,
    val token: String
)