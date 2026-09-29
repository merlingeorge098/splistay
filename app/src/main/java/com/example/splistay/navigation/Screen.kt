package com.example.splistay.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null) {
    object Onboarding : Screen("onboarding", "Onboarding")
    object Auth : Screen("auth", "Auth")
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Expenses : Screen("expenses", "Expenses", Icons.Default.List)
    object Chores : Screen("chores", "Chores", Icons.Default.DateRange)
    object AiAssistant : Screen("ai_assistant", "AI Assistant", Icons.Default.Face)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
    
    // Details
    object AddExpense : Screen("add_expense", "Add Expense")
    object Settlement : Screen("settlement", "Settlement")
    object GroceryFinder : Screen("grocery_finder", "Grocery Finder")
    object Roommates : Screen("roommates", "Roommates")
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Expenses,
    Screen.Chores,
    Screen.AiAssistant,
    Screen.Profile
)
