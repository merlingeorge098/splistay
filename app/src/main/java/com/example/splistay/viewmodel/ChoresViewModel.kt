package com.example.splistay.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splistay.data.model.Chore
import com.example.splistay.data.repository.SplitStayRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ChoresUiState(
    val pendingChores: List<Chore> = emptyList(),
    val todayChores: List<Chore> = emptyList(),
    val completedChores: List<Chore> = emptyList()
)

class ChoresViewModel(private val repository: SplitStayRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ChoresUiState())
    val uiState: StateFlow<ChoresUiState> = _uiState.asStateFlow()

    fun loadChores(roomId: String) {
        viewModelScope.launch {
            repository.getChores(roomId).collect { chores ->
                _uiState.value = ChoresUiState(
                    pendingChores = chores.filter { it.status == "pending" },
                    todayChores = chores.filter { it.status == "today" },
                    completedChores = chores.filter { it.status == "completed" }
                )
            }
        }
    }

    fun updateChoreStatus(chore: Chore, newStatus: String) {
        viewModelScope.launch {
            repository.updateChore(chore.copy(status = newStatus))
        }
    }
    
    fun verifyChore(chore: Chore, photoUri: String, lat: Double, lng: Double) {
        viewModelScope.launch {
            // Distance check logic would go here
            repository.updateChore(chore.copy(
                status = "completed",
                proofPhotoUri = photoUri,
                proofLatitude = lat,
                proofLongitude = lng,
                proofTimestamp = System.currentTimeMillis(),
                gpsVerified = true // Mock verification success
            ))
        }
    }
}
