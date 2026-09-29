package com.example.splistay.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.splistay.ServiceLocator
import com.example.splistay.ui.screens.auth.AuthScreen
import com.example.splistay.ui.screens.expenses.AddExpenseScreen
import com.example.splistay.ui.screens.expenses.ExpensesScreen
import com.example.splistay.ui.screens.home.HomeDashboard
import com.example.splistay.ui.screens.onboarding.OnboardingScreen
import com.example.splistay.ui.screens.chores.ChoreManager
import com.example.splistay.ui.screens.ai.AiAssistant
import com.example.splistay.viewmodel.ExpensesViewModel
import com.example.splistay.viewmodel.HomeViewModel
import com.example.splistay.viewmodel.ChoresViewModel
import com.example.splistay.viewmodel.AiAssistantViewModel

@Composable
fun NavGraph(navController: NavHostController) {
    val context = LocalContext.current
    val repository = remember { ServiceLocator.provideRepository(context) }
    
    // Simplistic ViewModel providing. In production, use Hilt or a better factory.
    val homeViewModel: HomeViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(repository) as T
        }
    })
    val expensesViewModel: ExpensesViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            return ExpensesViewModel(repository) as T
        }
    })
    val choresViewModel: ChoresViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            return ChoresViewModel(repository) as T
        }
    })
    val aiViewModel: AiAssistantViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            return AiAssistantViewModel(repository) as T
        }
    })

    NavHost(
        navController = navController,
        startDestination = Screen.Onboarding.route
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onFinish = { navController.navigate(Screen.Auth.route) },
                onGuest = { navController.navigate(Screen.Home.route) }
            )
        }
        composable(Screen.Auth.route) {
            AuthScreen(onSuccess = { navController.navigate(Screen.Home.route) })
        }
        
        composable(Screen.Home.route) { 
            HomeDashboard(
                viewModel = homeViewModel,
                onAddExpense = { navController.navigate(Screen.AddExpense.route) },
                onAddChore = { navController.navigate(Screen.Chores.route) },
                onSettleUp = { navController.navigate(Screen.Settlement.route) },
                onGroceryRun = { navController.navigate(Screen.GroceryFinder.route) }
            )
        }
        composable(Screen.Expenses.route) { 
            ExpensesScreen(
                viewModel = expensesViewModel,
                onAddExpense = { navController.navigate(Screen.AddExpense.route) }
            )
        }
        composable(Screen.AddExpense.route) {
            AddExpenseScreen(
                viewModel = expensesViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Chores.route) { 
            ChoreManager(
                viewModel = choresViewModel,
                onAddChore = { /* navController.navigate(Screen.AddChore.route) */ },
                onUploadProof = { chore -> 
                    // viewModel.verifyChore(chore, "uri", 0.0, 0.0) 
                }
            )
        }
        composable(Screen.AiAssistant.route) { 
            AiAssistant(viewModel = aiViewModel)
        }
        composable(Screen.Profile.route) { PlaceholderScreen(Screen.Profile.title) }
        composable(Screen.Settlement.route) { PlaceholderScreen(Screen.Settlement.title) }
        composable(Screen.GroceryFinder.route) { PlaceholderScreen(Screen.GroceryFinder.title) }
        composable(Screen.Roommates.route) { PlaceholderScreen(Screen.Roommates.title) }
    }
}

@Composable
fun PlaceholderScreen(title: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "$title Screen coming soon")
    }
}
