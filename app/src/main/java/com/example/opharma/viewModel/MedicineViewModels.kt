package com.example.opharma.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.opharma.data.remote.ApiService
import com.example.opharma.data.state.UiState


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MedicineViewModel : ViewModel() {
    private val api = ApiService()

    private val _medicines = MutableStateFlow<UiState<String>>(UiState.Loading)
    val medicines: StateFlow<UiState<String>> = _medicines.asStateFlow()

    private val _medicineDetail = MutableStateFlow<UiState<String>>(UiState.Loading)
    val medicineDetail: StateFlow<UiState<String>> = _medicineDetail.asStateFlow()

    private val _compatibilityData = MutableStateFlow<UiState<String>>(UiState.Loading)
    val compatibilityData: StateFlow<UiState<String>> = _compatibilityData.asStateFlow()

    fun loadMedicines() {
        viewModelScope.launch {
            _medicines.value = UiState.Loading
            val result = api.getMedicines()
            _medicines.value = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Ошибка загрузки лекарств") }
            )
        }
    }

    fun loadMedicineById(id: Int) {
        viewModelScope.launch {
            _medicineDetail.value = UiState.Loading
            val result = api.getMedicineById(id)
            _medicineDetail.value = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Ошибка загрузки лекарства") }
            )
        }
    }

    fun loadCompatibility(medicineId: Int) {
        viewModelScope.launch {
            _compatibilityData.value = UiState.Loading
            val result = api.getCompatibility(medicineId)
            _compatibilityData.value = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Ошибка загрузки совместимости") }
            )
        }
    }
}