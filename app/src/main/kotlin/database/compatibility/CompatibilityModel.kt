package database.compatibility

import org.jetbrains.exposed.sql.Table

object CompatibilityModel : Table("compatibility") {
    val id = integer("id").autoIncrement()
    val medicineId = integer("medicine_id")
    val type = varchar("type", 50)
    val itemName = varchar("item_name", 255)
    val compatibilityStatus = varchar("compatibility_status", 50)
    val recommendation = text("recommendation").nullable()

    override val primaryKey = PrimaryKey(id)
}