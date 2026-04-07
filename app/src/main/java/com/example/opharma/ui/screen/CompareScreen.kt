package com.example.opharma.ui.screen

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
import com.example.opharma.data.model.CompatibilityStatus
import com.example.opharma.data.model.Tablet
import com.example.opharma.viewModel.MedicineViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompareScreen(viewModel: MedicineViewModel) {

    var selectedTablets by remember { mutableStateOf(listOf<Tablet?>()) }
    var result by remember { mutableStateOf<String?>(null) }

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


        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.height(300.dp)  // ограничим высоту
        ) {
            itemsIndexed(selectedTablets) { index, selectedTablet ->
                TabletComparisonRow(
                    tablet = selectedTablet,
                    allTablets = viewModel.sampleTablets,
                    onTabletSelected = { newTablet ->
                        val newList = selectedTablets.toMutableList()
                        newList[index] = newTablet
                        selectedTablets = newList
                    },
                    onRemove = {
                        selectedTablets = selectedTablets.toMutableList().apply {
                            removeAt(index)
                        }
                    }
                )
            }
        }

        // Кнопка добавления препарата
        Button(
            onClick = {
                selectedTablets = selectedTablets + null  // добавляем пустой слот
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Добавить препарат")
        }

        Button(
            onClick = {
                val validTablets = selectedTablets.filterNotNull()
                if (validTablets.size >= 2) {
                    result = compareMultipleTablets(validTablets)
                } else {
                    result = "Выберите минимум 2 препарата"
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedTablets.filterNotNull().size >= 2
        ) {
            Text("Сравнить все")
        }

        result?.let {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = it,
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TabletComparisonRow(
    tablet: Tablet?,
    allTablets: List<Tablet>,
    onTabletSelected: (Tablet) -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        // Dropdown выбора препарата
        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.weight(1f)
        ) {
            OutlinedTextField(
                value = tablet?.name ?: "Выберите препарат",
                onValueChange = { },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                allTablets.forEach { t ->
                    DropdownMenuItem(
                        text = { Text(t.name) },
                        onClick = {
                            onTabletSelected(t)
                            expanded = false
                        }
                    )
                }
            }
        }

        // Кнопка удаления
        IconButton(onClick = onRemove) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Удалить препарат",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

private fun compareMultipleTablets(tablets: List<Tablet>): String {
    val scores = tablets.associate { it.name to it.compatibility.count { info -> info.status == CompatibilityStatus.COMPATIBLE } }

    val bestTablet = scores.maxByOrNull { it.value }?.key ?: "Нет данных"
    val bestScore = scores.values.maxOrNull() ?: 0

    val worstTablet = scores.minByOrNull { it.value }?.key ?: "Нет данных"
    val worstScore = scores.values.minOrNull() ?: 0

    return """
        Лучшая: $bestTablet ($bestScore пунктов «Можно»)
        Худшая: $worstTablet ($worstScore пунктов «Можно»)
        
        ${tablets.size} препаратов сравнено
    """.trimIndent()
}