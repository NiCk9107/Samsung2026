package com.example.Opharma.data.model

import kotlinx.serialization.Serializable

object MedicineModels {
    @Serializable
    data class Medicine(
        val id: Int,
        val name: String,
        val description: String? = null,
        val foodCompatibility: String? = null,
        val alcoholCompatibility: String? = null
    )

    @Serializable
    data class Compatibility(
        val id: Int,
        val medicineId: Int,
        val type: String,
        val itemName: String,
        val compatibilityStatus: String,
        val recommendation: String? = null
    )
}