package features.medicines

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureMedicineRouting() {
    routing {
        get("/medicines") {
            val controller = MedicineController(call)
            controller.getAllMedicines()
        }

        get("/medicines/{id}") {
            val controller = MedicineController(call)
            controller.getMedicineById()
        }
    }
}