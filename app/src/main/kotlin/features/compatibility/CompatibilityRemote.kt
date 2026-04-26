package features.compatibility

import database.compatibility.CompatibilityDTO
import kotlinx.serialization.Serializable

@Serializable
data class CompatibilityReceiveRemote(
    val medicineId: Int
)

@Serializable
data class CompatibilityResponseRemote(
    val compatibilities: List<CompatibilityDTO>
)
