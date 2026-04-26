package features.compatibility

import database.compatibility.CompatibilityDTO
import database.compatibility.CompatibilityModel
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class CompatibilityController(private val call: ApplicationCall) {

    suspend fun getByMedicineId() {
        val id = call.parameters["id"]?.toIntOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid medicine ID")
            return
        }

        val compatibilities = transaction {
            CompatibilityModel.selectAll()
                .where { CompatibilityModel.medicineId eq id }
                .map {
                    CompatibilityDTO(
                        id = it[CompatibilityModel.id],
                        medicineId = it[CompatibilityModel.medicineId],
                        type = it[CompatibilityModel.type],
                        itemName = it[CompatibilityModel.itemName],
                        compatibilityStatus = it[CompatibilityModel.compatibilityStatus],
                        recommendation = it[CompatibilityModel.recommendation]
                    )
                }
        }
        call.respond(compatibilities)
    }
}