package database.users

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: Int,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String,
    val password: String,
    val phone: String? = null,
    val age: Int? = null,
    val allergies: String? = null
)