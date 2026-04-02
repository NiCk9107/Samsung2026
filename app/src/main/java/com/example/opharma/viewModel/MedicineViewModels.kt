package com.example.opharma.viewModel

import androidx.lifecycle.ViewModel
import com.example.opharma.data.model.Category
import com.example.opharma.data.model.CompatibilityInfo
import com.example.opharma.data.model.CompatibilityStatus
import com.example.opharma.data.model.Tablet

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MedicineViewModel : ViewModel() {

    private val _selectedTablet = MutableStateFlow<Tablet?>(null)
    val selectedTablet: StateFlow<Tablet?> = _selectedTablet

    val categories = listOf(
        Category(1, "Таблетки"),
        Category(2, "Еда"),
        Category(3, "Напитки")
    )

    val sampleTablets = listOf(
        Tablet(
            id = 1,
            name = "Аспирин",
            description = "Обезболивающее и противовоспалительное средство",
            compatibility = listOf(
                CompatibilityInfo(
                    "Совместимость с едой",
                    CompatibilityStatus.COMPATIBLE,
                    "Можно принимать с пищей"
                ),
                CompatibilityInfo(
                    "Совместимость с алкоголем",
                    CompatibilityStatus.INCOMPATIBLE,
                    "Не рекомендуется"
                ),
                CompatibilityInfo(
                    "Время приема",
                    CompatibilityStatus.CAUTION,
                    "Лучше после еды"
                )
            )
        )
    )

    fun selectTablet(tablet: Tablet) {
        _selectedTablet.value = tablet
    }

    fun checkCompatibility(): Boolean {
        return _selectedTablet.value != null
    }
}