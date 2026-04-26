package features.medicines

import database.medicines.MedicineDTO
import database.medicines.MedicineModel
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class MedicineController(private val call: ApplicationCall) {

    suspend fun getAllMedicines() {
        val medicines = transaction {
            MedicineModel.selectAll().map {
                MedicineDTO(
                    id = it[MedicineModel.id],
                    name = it[MedicineModel.name],
                    description = it[MedicineModel.description],
                    foodCompatibility = it[MedicineModel.foodCompatibility],
                    alcoholCompatibility = it[MedicineModel.alcoholCompatibility]
                )
            }
        }
        call.respond(medicines)
    }

    suspend fun getMedicineById() {
        val id = call.parameters["id"]?.toIntOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid ID")
            return
        }

        val medicine = transaction {
            MedicineModel.selectAll()
                .where { MedicineModel.id eq id }
                .map {
                    MedicineDTO(
                        id = it[MedicineModel.id],
                        name = it[MedicineModel.name],
                        description = it[MedicineModel.description],
                        foodCompatibility = it[MedicineModel.foodCompatibility],
                        alcoholCompatibility = it[MedicineModel.alcoholCompatibility]
                    )
                }.singleOrNull()
        }

        if (medicine != null) {
            call.respond(medicine)
        } else {
            call.respond(HttpStatusCode.NotFound, "Medicine not found")
        }
    }
}