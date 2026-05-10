package com.example.opharma.navigation

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.opharma.ui.screens.CompareScreen
import com.example.opharma.ui.screens.DrinkDetailScreen
import com.example.opharma.ui.screens.FoodDetailScreen
import com.example.opharma.ui.screens.LoginScreen
import com.example.opharma.ui.screens.MainTabScreen
import com.example.opharma.ui.screens.OtherMedsDetailScreen
import com.example.opharma.ui.screens.ProfileScreen
import com.example.opharma.ui.screens.RegisterScreen
import com.example.opharma.ui.screens.TabletDetailScreen
import com.example.opharma.viewModel.MedicineViewModel


private const val PREFS_NAME = "app_prefs"
private const val KEY_AUTH_TOKEN = "auth_token"

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val medicineViewModel: MedicineViewModel = viewModel()
    val context = LocalContext.current

    val startDestination = if (hasAuthToken(context)) "home" else "login"

    NavHost(navController = navController, startDestination = startDestination) {

        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    saveAuthToken(context, "token")
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate("register") }
            )
        }

        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    saveAuthToken(context, "token")
                    navController.navigate("home") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("home") {
            MainTabScreen(
                viewModel = medicineViewModel,
                navToResult = { id -> navController.navigate("result/$id") },
                navToTabletDetail = { id -> navController.navigate("detail/$id") },
                onProfileClick = { navController.navigate("profile") }
            )
        }

        composable("detail/{medicineId}") { entry ->
            val id = entry.arguments?.getString("medicineId")?.toIntOrNull() ?: 0
            TabletDetailScreen(
                viewModel = medicineViewModel,
                medicineId = id,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToFoodDetail = { navController.navigate("food/$id") },
                onNavigateToDrinkDetail = { navController.navigate("drink/$id") },
                onNavigateToOtherMedsDetail = { navController.navigate("othermeds/$id") }
            )
        }

        composable("food/{medicineId}") { entry ->
            val id = entry.arguments?.getString("medicineId")?.toIntOrNull() ?: 0
            FoodDetailScreen(viewModel = medicineViewModel, medicineId = id, onNavigateBack = { navController.popBackStack() })
        }

        composable("drink/{medicineId}") { entry ->
            val id = entry.arguments?.getString("medicineId")?.toIntOrNull() ?: 0
            DrinkDetailScreen(viewModel = medicineViewModel, medicineId = id, onNavigateBack = { navController.popBackStack() })
        }

        composable("othermeds/{medicineId}") { entry ->
            val id = entry.arguments?.getString("medicineId")?.toIntOrNull() ?: 0
            OtherMedsDetailScreen(viewModel = medicineViewModel, medicineId = id, onNavigateBack = { navController.popBackStack() })
        }

        composable("result/{medicineId}") { entry ->
            val id = entry.arguments?.getString("medicineId")?.toIntOrNull() ?: 0
            CompareScreen(viewModel = medicineViewModel, medicineId = id, onNavigateBack = { navController.popBackStack() })
        }
        composable("profile") {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                onLogout = {
                    clearAuthToken(context)
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}

fun hasAuthToken(context: Context): Boolean {
    return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).contains(KEY_AUTH_TOKEN)
}

fun saveAuthToken(context: Context, token: String) {
    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().putString(KEY_AUTH_TOKEN, token).apply()
}

fun clearAuthToken(context: Context) {
    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().remove(KEY_AUTH_TOKEN).apply()
}