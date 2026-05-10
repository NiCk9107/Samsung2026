package com.example.opharma.ui.screens

import androidx.compose.foundation.layout.*
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
fun HomeScreen(
    viewModel: MedicineViewModel,
    onNavigateToDetail: () -> Unit,
    onCheckClick: () -> Unit
) {
    val medicinesState by viewModel.medicines.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadMedicines()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Главный", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Таблетки",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )

            when (val state = medicinesState) {
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
                    val medicineList = try {
                        Json.parseToJsonElement(state.data).jsonArray
                    } catch (e: Exception) {
                        emptyList()
                    }

                    if (medicineList.isNotEmpty()) {
                        medicineList.forEach { element ->
                            val obj = element.jsonObject
                            val id = obj["id"]?.jsonPrimitive?.int ?: 0
                            val name = obj["name"]?.jsonPrimitive?.content ?: ""
                            val description = obj["description"]?.jsonPrimitive?.content ?: ""

                            Card(
                                onClick = {
                                    viewModel.loadMedicineById(id)
                                    onNavigateToDetail()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(text = name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                                    Text(text = description, fontSize = 14.sp, color = Color.Gray)
                                }
                            }
                        }
                    } else {
                        Text("Нет данных")
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}