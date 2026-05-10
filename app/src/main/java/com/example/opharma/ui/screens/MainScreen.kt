package com.example.opharma.ui.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.opharma.data.state.UiState
import com.example.opharma.viewModel.MedicineViewModel

import kotlinx.serialization.json.*

data class MedicineItem(val id: Int, val name: String, val description: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MedicineViewModel,
    navToResult: (Int) -> Unit,
    navToTabletDetail: (Int) -> Unit,
    onProfileClick: () -> Unit
) {
    val medicinesState by viewModel.medicines.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadMedicines()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Справочник лекарств", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(Icons.Default.Person, contentDescription = "Профиль")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
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
                    val medicines = try {
                        Log.d("PHARMA", "JSON: ${state.data}")
                        if (state.data.startsWith("[")) {
                            val jsonArray = Json.parseToJsonElement(state.data).jsonArray
                            jsonArray.map { element ->
                                val obj = element.jsonObject
                                MedicineItem(
                                    id = obj["id"]?.jsonPrimitive?.int ?: 0,
                                    name = obj["name"]?.jsonPrimitive?.content ?: "",
                                    description = obj["description"]?.jsonPrimitive?.content ?: ""
                                )
                            }
                        } else {
                            emptyList()
                        }
                    } catch (e: Exception) {
                        Log.e("PHARMA", "Parse error: ${e.message}")
                        emptyList()
                    }

                    Text(
                        text = "Таблетки",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (medicines.isNotEmpty()) {
                        LazyColumn {
                            items(medicines) { med ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clickable { navToTabletDetail(med.id) }
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(med.name, fontWeight = FontWeight.SemiBold)
                                        Text(
                                            med.description,
                                            fontSize = 14.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
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