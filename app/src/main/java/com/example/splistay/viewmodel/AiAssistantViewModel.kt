package com.example.splistay.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splistay.data.repository.SplitStayRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ChatMessage(
    val text: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

data class AiUiState(
    val chatMessages: List<ChatMessage> = listOf(
        ChatMessage("Hi! I'm SplitStay AI. How can I help you manage your shared space today?", false)
    ),
    val monthlyTotal: Double = 4250.0,
    val forecastAmount: Double = 1850.0,
    val chartData: List<Pair<String, Float>> = listOf(
        "W1" to 90f, "W2" to 80f, "W3" to 40f, "W4" to 70f
    )
)

class AiAssistantViewModel(private val repository: SplitStayRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AiUiState())
    val uiState: StateFlow<AiUiState> = _uiState.asStateFlow()

    fun sendMessage(text: String) {
        val userMessage = ChatMessage(text, true)
        val currentMessages = _uiState.value.chatMessages + userMessage
        _uiState.value = _uiState.value.copy(chatMessages = currentMessages)

        viewModelScope.launch {
            val responseText = generateBotResponse(text)
            val botMessage = ChatMessage(responseText, false)
            _uiState.value = _uiState.value.copy(chatMessages = _uiState.value.chatMessages + botMessage)
        }
    }

    private fun generateBotResponse(input: String): String {
        return when {
            input.contains("owe", ignoreCase = true) -> {
                "Based on this month's logs:\n• Rahul owes you ₹220 (Internet).\n• You owe Priya ₹150 (Groceries)."
            }
            input.contains("rent", ignoreCase = true) -> {
                "Your next rent of ₹12,000 is due on 1st October."
            }
            input.contains("chore", ignoreCase = true) -> {
                "There are 2 chores pending for today: Clean Bathroom and Garbage."
            }
            else -> "I can help you track expenses, check chore status, or predict bills. Try asking 'Who owes whom?'"
        }
    }
}
