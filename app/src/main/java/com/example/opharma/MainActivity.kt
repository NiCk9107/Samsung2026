package com.example.opharma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.opharma.ui.theme.OpharmaTheme
import com.example.opharma.viewModel.MedicineViewModel
import com.example.opharma.navigation.AppNavigation



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OpharmaTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    val medicineViewModel: MedicineViewModel = viewModel()

                    // Загружаем лекарства при старте
                    medicineViewModel.loadMedicines()

                    AppNavigation()
                }
            }
        }
    }
}