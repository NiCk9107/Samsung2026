package database.medicines

import kotlinx.serialization.Serializable

@Serializable
data class MedicineDTO(
    val id: Int,
    val name: String,
    val description: String?,
    val foodCompatibility: String?,
    val alcoholCompatibility: String?
)