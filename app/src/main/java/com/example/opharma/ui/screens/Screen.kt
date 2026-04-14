package com.example.opharma.ui.screens


sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object Detail : Screen("detail")
    object Result : Screen("result")
    object FoodDetail : Screen("food_detail")
    object DrinkDetail : Screen("drink_detail")
    object OtherMedsDetail : Screen("other_meds_detail")
    object Profile : Screen("profile")
}