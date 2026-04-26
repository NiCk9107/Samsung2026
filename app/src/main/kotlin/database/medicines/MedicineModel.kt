package database.medicines

import org.jetbrains.exposed.sql.Table

object MedicineModel : Table("medicines") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)
    val description = varchar("description", 500).nullable()
    val foodCompatibility = varchar("food_compatibility", 50).nullable()
    val alcoholCompatibility = varchar("alcohol_compatibility", 50).nullable()

    override val primaryKey = PrimaryKey(id)
}