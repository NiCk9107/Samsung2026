package com.example.opharma.navigation

import android.content.Context
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.opharma.ui.screens.*
import com.example.opharma.viewModel.MedicineViewModel

private const val PREFS_NAME = "app_prefs"
private const val KEY_FIRST_LAUNCH = "first_launch"
private const val KEY_AUTH_TOKEN = "auth_token"

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val medicineViewModel: MedicineViewModel = viewModel()
    val context = LocalContext.current
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    val startDestination by remember {
        mutableStateOf(
            when {
                !hasAuthToken(context) -> Screen.Login.route
                isFirstLaunch(context) -> Screen.Onboarding.route
                else -> Screen.Home.route
            }
        )
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // LOGIN
        composable(
            route = Screen.Login.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start
                ) + fadeIn()
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End
                ) + fadeOut()
            }
        ) {
            LoginScreen(
                onLoginSuccess = {
                    saveAuthToken(context, "user_token_${System.currentTimeMillis()}")
                    if (isFirstLaunch(context)) {
                        navController.navigate(Screen.Onboarding.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                },
                onRegisterClick = { navController.navigate(Screen.Register.route) }
            )
        }

        // REGISTER
        composable(
            route = Screen.Register.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start
                ) + fadeIn()
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End
                ) + fadeOut()
            }
        ) {
            RegisterScreen(
                onRegisterSuccess = {
                    saveAuthToken(context, "user_token_${System.currentTimeMillis()}")
                    if (isFirstLaunch(context)) {
                        navController.navigate(Screen.Onboarding.route) {
                            popUpTo(Screen.Register.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Register.route) { inclusive = true }
                        }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        // ONBOARDING
        composable(
            route = Screen.Onboarding.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start
                ) + fadeIn()
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End
                ) + fadeOut()
            }
        ) {
            OnboardingScreen(
                onFinish = {
                    setOnboardingShown(context)
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        // HOME
        composable(
            route = Screen.Home.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start
                ) + fadeIn()
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End
                ) + fadeOut()
            }
        ) {
            MainTabScreen(
                viewModel = medicineViewModel,
                navToResult = { navController.navigate(Screen.Result.route) },
                navToTabletDetail = { navController.navigate(Screen.Detail.route) },
                onProfileClick = { navController.navigate(Screen.Profile.route) }
            )
        }

        // DETAIL
        composable(
            route = Screen.Detail.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start
                ) + fadeIn()
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End
                ) + fadeOut()
            }
        ) {
            TabletDetailScreen(
                viewModel = medicineViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToFoodDetail = { navController.navigate(Screen.FoodDetail.route) },
                onNavigateToDrinkDetail = { navController.navigate(Screen.DrinkDetail.route) },
                onNavigateToOtherMedsDetail = { navController.navigate(Screen.OtherMedsDetail.route) }
            )
        }

        composable(
            route = Screen.FoodDetail.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start
                ) + fadeIn()
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End
                ) + fadeOut()
            }
        ) {
            FoodDetailScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(
            route = Screen.DrinkDetail.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start
                ) + fadeIn()
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End
                ) + fadeOut()
            }
        ) {
            DrinkDetailScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(
            route = Screen.OtherMedsDetail.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start
                ) + fadeIn()
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End
                ) + fadeOut()
            }
        ) {
            OtherMedsDetailScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(
            route = Screen.Result.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start
                ) + fadeIn()
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End
                ) + fadeOut()
            }
        ) {
            ResultScreen(
                viewModel = medicineViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Profile.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start
                ) + fadeIn()
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End
                ) + fadeOut()
            }
        ) {
            ProfileScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}

// SharedPreferences остаются без изменений
fun hasAuthToken(context: Context): Boolean {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return prefs.contains(KEY_AUTH_TOKEN)
}

fun saveAuthToken(context: Context, token: String) {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    prefs.edit().putString(KEY_AUTH_TOKEN, token).apply()
}

fun isFirstLaunch(context: Context): Boolean {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return prefs.getBoolean(KEY_FIRST_LAUNCH, true)
}

fun setOnboardingShown(context: Context) {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    prefs.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply()
}