package com.example.opharma.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.opharma.data.state.UiState
import com.example.opharma.viewModel.MedicineViewModel

import kotlinx.serialization.json.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabletDetailScreen(
    viewModel: MedicineViewModel,
    medicineId: Int,
    onNavigateBack: () -> Unit,
    onNavigateToFoodDetail: () -> Unit,
    onNavigateToDrinkDetail: () -> Unit,
    onNavigateToOtherMedsDetail: () -> Unit
) {
    val medicineDetailState by viewModel.medicineDetail.collectAsState()
    val compatibilityState by viewModel.compatibilityData.collectAsState()

    LaunchedEffect(medicineId) {
        viewModel.loadMedicineById(medicineId)
        viewModel.loadCompatibility(medicineId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val title = (medicineDetailState as? UiState.Success)?.let { parseName(it.data) } ?: "Детали"
                    Text(title, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            // Описание
            when (val state = medicineDetailState) {
                is UiState.Loading -> Text("Загрузка описания...")
                is UiState.Error -> Text(state.message, color = MaterialTheme.colorScheme.error)
                is UiState.Success -> Text(parseDescription(state.data), fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопки
            Button(onClick = onNavigateToFoodDetail, modifier = Modifier.fillMaxWidth()) { Text("Еда") }
            Button(onClick = onNavigateToDrinkDetail, modifier = Modifier.fillMaxWidth()) { Text("Напитки") }
            Button(onClick = onNavigateToOtherMedsDetail, modifier = Modifier.fillMaxWidth()) { Text("Другие лекарства") }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Совместимость", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)

            // Совместимость
            when (val state = compatibilityState) {
                is UiState.Loading -> Text("Загрузка...")
                is UiState.Error -> Text(state.message, color = MaterialTheme.colorScheme.error)
                is UiState.Success -> {
                    val items = parseCompatibilities(state.data)
                    if (items.isNotEmpty()) {
                        items.forEach { element ->
                            val obj = element.jsonObject
                            val itemName = obj["itemName"]?.jsonPrimitive?.content ?: ""
                            val status = obj["compatibilityStatus"]?.jsonPrimitive?.content ?: ""
                            val recommendation = obj["recommendation"]?.jsonPrimitive?.content ?: ""

                            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(itemName, fontWeight = FontWeight.SemiBold)
                                    Text(status, color = when (status) {
                                        "Можно" -> Color(0xFF4CAF50)
                                        "Нельзя" -> Color(0xFFF44336)
                                        else -> Color(0xFFFFC107)
                                    })
                                    if (recommendation.isNotBlank()) {
                                        Text(recommendation, fontSize = 12.sp, color = Color.Gray)
                                    }
                                }
                            }
                        }
                    } else {
                        Text("Нет данных")
                    }
                }
            }
        }
    }
}

// Вспомогательные функции парсинга вне Composable
private fun parseName(json: String): String = runCatching {
    Json.parseToJsonElement(json).jsonObject["name"]?.jsonPrimitive?.content ?: "Детали"
}.getOrDefault("Детали")

private fun parseDescription(json: String): String = runCatching {
    Json.parseToJsonElement(json).jsonObject["description"]?.jsonPrimitive?.content ?: ""
}.getOrDefault("")

private fun parseCompatibilities(json: String): List<JsonElement> = runCatching {
    Json.parseToJsonElement(json).jsonArray.toList()
}.getOrDefault(emptyList())