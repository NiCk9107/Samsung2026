package com.example.opharma.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.opharma.data.model.Tablet
import com.example.opharma.viewModel.MedicineViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MedicineViewModel,
    onNavigateToDetail: () -> Unit,
    onCheckClick: () -> Unit
) {
    val categories = viewModel.categories
    val tablets = viewModel.sampleTablets



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

            tablets.forEach { tablet ->
                TabletItem(
                    tablet = tablet,
                    onClick = {
                        viewModel.selectTablet(tablet)   // запоминаем выбранную
                        onNavigateToDetail()             // переходим на детали
                    }
                )
            }

            Spacer(modifier = Modifier.weight(1f))


        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabletItem(
    tablet: Tablet,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = tablet.name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Text(text = tablet.description, fontSize = 14.sp, color = Color.Gray)
        }
    }
}
