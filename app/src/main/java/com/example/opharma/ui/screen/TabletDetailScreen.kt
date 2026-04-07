package com.example.opharma.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.opharma.data.model.CompatibilityInfo
import com.example.opharma.data.model.CompatibilityStatus
import com.example.opharma.viewModel.MedicineViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabletDetailScreen(
    viewModel: MedicineViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToFoodDetail: () -> Unit,
    onNavigateToDrinkDetail: () -> Unit,
    onNavigateToOtherMedsDetail: () -> Unit
) {
    val selectedTablet by viewModel.selectedTablet.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(selectedTablet?.name ?: "Детали таблетки", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (selectedTablet == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Таблетка не выбрана")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = selectedTablet!!.description,
                    fontSize = 16.sp
                )

                // Кнопки с доп‑инфой
                Button(
                    onClick = onNavigateToFoodDetail,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Еда")
                }

                Button(
                    onClick = onNavigateToDrinkDetail,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Напитки")
                }

                Button(
                    onClick = onNavigateToOtherMedsDetail,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Другие лекарства")
                }

                Text(
                    text = "Совместимость",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )

                selectedTablet!!.compatibility.forEach { info ->
                    CompatibilityItem(info = info)
                }
            }
        }
    }
}
@Composable
fun CompatibilityItem(info: CompatibilityInfo) {
    val statusText = when (info.status) {
        CompatibilityStatus.COMPATIBLE -> "Можно"
        CompatibilityStatus.INCOMPATIBLE -> "Нельзя"
        CompatibilityStatus.CAUTION -> "С осторожностью"
    }

    val statusColor = when (info.status) {
        CompatibilityStatus.COMPATIBLE -> Color(0xFF4CAF50) // зелёный
        CompatibilityStatus.INCOMPATIBLE -> Color(0xFFF44336) // красный
        CompatibilityStatus.CAUTION -> Color(0xFFFFC107) // жёлтый
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = info.title, fontWeight = FontWeight.SemiBold)
            Text(text = statusText, color = statusColor)
            if (info.description.isNotBlank()) {
                Text(text = info.description, fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}