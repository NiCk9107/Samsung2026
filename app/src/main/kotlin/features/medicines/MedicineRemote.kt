package features.medicines

import database.medicines.MedicineDTO
import kotlinx.serialization.Serializable

@Serializable
data class MedicineReceiveRemote(
    val id: Int
)

@Serializable
data class MedicineResponseRemote(
    val medicines: List<MedicineDTO>
)
