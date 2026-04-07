package com.example.opharma.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.opharma.viewModel.MedicineViewModel

enum class BottomTab {
    HOME, FAVORITES, COMPARE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTabScreen(
    viewModel: MedicineViewModel,
    navToResult: () -> Unit,
    navToTabletDetail: () -> Unit,
    onProfileClick: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(BottomTab.HOME) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == BottomTab.HOME,
                    onClick = { selectedTab = BottomTab.HOME },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Дом") },
                    label = { Text("Справочник") }
                )
                NavigationBarItem(
                    selected = selectedTab == BottomTab.FAVORITES,
                    onClick = { selectedTab = BottomTab.FAVORITES },
                    icon = { Icon(Icons.Default.PlayArrow, contentDescription = "Избранное") },
                    label = { Text("Избранное") }
                )
                NavigationBarItem(
                    selected = selectedTab == BottomTab.COMPARE,
                    onClick = { selectedTab = BottomTab.COMPARE },
                    icon = { Icon(Icons.Default.List, contentDescription = "Сравнение") },
                    label = { Text("Сравнение") }
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedTab) {
                BottomTab.HOME -> MainScreen(
                    viewModel = viewModel,
                    navToResult = navToResult,
                    navToTabletDetail = navToTabletDetail,
                    onProfileClick = onProfileClick
                )
                BottomTab.FAVORITES -> FavoritesScreen(viewModel)
                BottomTab.COMPARE -> CompareScreen(viewModel)
            }
        }
    }
}