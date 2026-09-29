package com.example.splistay.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splistay.data.model.Expense
import com.example.splistay.data.repository.SplitStayRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ExpensesUiState(
    val expenses: List<Expense> = emptyList(),
    val youOwe: Double = 0.0,
    val youGet: Double = 0.0
)

class ExpensesViewModel(private val repository: SplitStayRepository) : ViewModel() {

    val uiState: StateFlow<ExpensesUiState> = repository.getExpenses("default_room")
        .map { expenses ->
            ExpensesUiState(
                expenses = expenses,
                youOwe = 320.0,
                youGet = 540.0
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ExpensesUiState()
        )

    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            repository.addExpense(expense)
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            repository.deleteExpense(expense)
        }
    }
}
