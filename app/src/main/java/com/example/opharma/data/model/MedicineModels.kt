package com.example.opharma.data.model

data class Tablet(
    val id: Int,
    val name: String,
    val description: String,
    val compatibility: List<CompatibilityInfo>
)

data class CompatibilityInfo(
    val title: String,
    val status: CompatibilityStatus,
    val description: String
)

enum class CompatibilityStatus {
    COMPATIBLE,
    INCOMPATIBLE,
    CAUTION
}

data class Category(
    val id: Int,
    val name: String,
    val icon: Int? = null
)