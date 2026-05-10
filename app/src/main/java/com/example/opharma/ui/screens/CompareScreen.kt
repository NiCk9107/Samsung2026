package com.example.opharma.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.opharma.data.state.UiState
import com.example.opharma.viewModel.MedicineViewModel

import kotlinx.serialization.json.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompareScreen(
    viewModel: MedicineViewModel,
    medicineId: Int = 0,
    onNavigateBack: () -> Unit = {}
) {
    val medicinesState by viewModel.medicines.collectAsState()
    var selectedTablets by remember { mutableStateOf<List<String?>>(emptyList()) }
    var result by remember { mutableStateOf<String?>(null) }

    val medicineNames = remember(medicinesState) {
        when (val state = medicinesState) {
            is UiState.Success -> {
                val jsonText = state.data
                if (jsonText.isNotBlank()) {
                    try {
                        val jsonArray = Json.parseToJsonElement(jsonText).jsonArray
                        jsonArray.map { it.jsonObject["name"]?.jsonPrimitive?.content ?: "" }
                    } catch (e: Exception) {
                        emptyList()
                    }
                } else {
                    emptyList()
                }
            }
            else -> emptyList()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadMedicines()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Сравнение таблеток",
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp
        )

        when (val state = medicinesState) {
            is UiState.Loading -> {
                Text("Загрузка списка препаратов...")
            }
            is UiState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error
                )
            }
            is UiState.Success -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.height(300.dp)
                ) {
                    itemsIndexed(selectedTablets) { index, selectedTablet ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            var expanded by remember { mutableStateOf(false) }

                            ExposedDropdownMenuBox(
                                expanded = expanded,
                                onExpandedChange = { expanded = it },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = selectedTablet ?: "Выберите препарат",
                                    onValueChange = { },
                                    readOnly = true,
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                                    modifier = Modifier.menuAnchor()
                                )

                                ExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    medicineNames.forEach { name ->
                                        DropdownMenuItem(
                                            text = { Text(name) },
                                            onClick = {
                                                val newList = selectedTablets.toMutableList()
                                                newList[index] = name
                                                selectedTablets = newList
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            IconButton(
                                onClick = {
                                    selectedTablets = selectedTablets.toMutableList().apply {
                                        removeAt(index)
                                    }
                                }
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Удалить",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }

                Button(
                    onClick = { selectedTablets = selectedTablets + null },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Добавить препарат")
                }

                Button(
                    onClick = {
                        val valid = selectedTablets.filterNotNull()
                        result = if (valid.size >= 2) {
                            "Лучшая: ${valid.first()}\nХудшая: ${valid.last()}\n${valid.size} препаратов сравнено"
                        } else {
                            "Выберите минимум 2 препарата"
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = selectedTablets.filterNotNull().size >= 2
                ) {
                    Text("Сравнить все")
                }

                result?.let {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = it,
                            modifier = Modifier.padding(16.dp),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
