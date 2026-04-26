package database.compatibility

import kotlinx.serialization.Serializable

@Serializable
data class CompatibilityDTO(
    val id: Int,
    val medicineId: Int,
    val type: String,
    val itemName: String,
    val compatibilityStatus: String,
    val recommendation: String?
)