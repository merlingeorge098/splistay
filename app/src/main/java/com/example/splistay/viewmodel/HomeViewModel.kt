package com.example.splistay.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splistay.data.model.*
import com.example.splistay.data.repository.SplitStayRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HomeUiState(
    val room: StayRoom? = null,
    val roommates: List<Roommate> = emptyList(),
    val totalBalance: Double = 0.0,
    val recentActivity: List<ActivityItem> = emptyList()
)

data class ActivityItem(
    val type: String, // "expense", "chore", "settlement"
    val title: String,
    val subtitle: String,
    val timestamp: Long,
    val user: String
)

class HomeViewModel(private val repository: SplitStayRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun loadData(roomId: String) {
        viewModelScope.launch {
            combine(
                repository.getRoom(roomId),
                repository.getRoommates(roomId),
                repository.getExpenses(roomId),
                repository.getChores(roomId)
            ) { room, roommates, expenses, chores ->
                HomeUiState(
                    room = room,
                    roommates = roommates,
                    totalBalance = expenses.sumOf { it.amount }, // Placeholder balance logic
                    recentActivity = (expenses.map { 
                        ActivityItem("expense", "Expense Added", "${it.description} - ₹${it.amount}", it.date, "Someone") 
                    } + chores.map { 
                        ActivityItem("chore", "Chore Completed", it.title, it.dueDate, "Someone") 
                    }).sortedByDescending { it.timestamp }.take(5)
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }
}
