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
import com.example.opharma.data.state.UiState


import com.example.opharma.viewModel.MedicineViewModel
import kotlinx.serialization.json.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompatibilityScreen(
    viewModel: MedicineViewModel,
    medicineId: Int,
    onNavigateBack: () -> Unit
) {
    val compatibilityState by viewModel.compatibilityData.collectAsState()

    LaunchedEffect(medicineId) {
        viewModel.loadCompatibility(medicineId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Совместимость", fontWeight = FontWeight.Bold) },
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
            when (val state = compatibilityState) {
                is UiState.Loading -> {
                    Text("Загрузка...")
                }

                is UiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                is UiState.Success -> {
                    val items = try {
                        Json.parseToJsonElement(state.data).jsonArray
                    } catch (e: Exception) {
                        null
                    }

                    if (items != null) {
                        items.forEach { element ->
                            val obj = element.jsonObject
                            val itemName = obj["itemName"]?.jsonPrimitive?.content ?: ""
                            val status = obj["compatibilityStatus"]?.jsonPrimitive?.content ?: ""
                            val recommendation = obj["recommendation"]?.jsonPrimitive?.content ?: ""

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(itemName, fontWeight = FontWeight.SemiBold)
                                    Text(
                                        status,
                                        color = when (status) {
                                            "Можно" -> Color(0xFF4CAF50)
                                            "Нельзя" -> Color(0xFFF44336)
                                            else -> Color(0xFFFFC107)
                                        }
                                    )
                                    if (recommendation.isNotBlank()) {
                                        Text(recommendation, color = Color.Gray)
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