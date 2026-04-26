package features.compatibility

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureCompatibilityRouting() {
    routing {
        get("/compatibility/{id}") {
            val compatibilityController = CompatibilityController(call)
            compatibilityController.getByMedicineId()
        }
    }
}