package com.example.splistay.data.repository

import com.example.splistay.data.local.*
import com.example.splistay.data.model.*
import com.example.splistay.network.ApiService
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class SplitStayRepository(
    private val stayRoomDao: StayRoomDao,
    private val roommateDao: RoommateDao,
    private val expenseDao: ExpenseDao,
    private val choreDao: ChoreDao,
    private val settlementDao: SettlementDao,
    private val apiService: ApiService,
    private val firestore: FirebaseFirestore?
) {
    // Helper to ensure parent StayRoom exists before inserting child entities (prevents FK constraint crash)
    private suspend fun ensureRoomExists(roomId: String) {
        try {
            stayRoomDao.insertRoom(
                StayRoom(
                    id = roomId,
                    roomName = "Default Room",
                    joinCode = "123456",
                    createdBy = "user_1",
                    createdAt = System.currentTimeMillis(),
                    pgLatitude = null,
                    pgLongitude = null
                )
            )
        } catch (e: Exception) {
            android.util.Log.e("SplitStayRepository", "Failed to ensure room exists: ${e.message}")
        }
    }

    // Rooms
    fun getRoom(roomId: String): Flow<StayRoom?> = stayRoomDao.getRoom(roomId)

    suspend fun createRoom(room: StayRoom) {
        try {
            stayRoomDao.insertRoom(room)
            firestore?.collection("rooms")?.document(room.id)?.set(room)?.await()
        } catch (e: Exception) {
            android.util.Log.e("SplitStayRepository", "Error creating room: ${e.message}")
        }
    }

    // Roommates
    fun getRoommates(roomId: String): Flow<List<Roommate>> = roommateDao.getRoommates(roomId)

    suspend fun addRoommate(roommate: Roommate) {
        try {
            ensureRoomExists(roommate.roomId)
            roommateDao.insertRoommate(roommate)
            firestore?.collection("roommates")?.document(roommate.id)?.set(roommate)?.await()
        } catch (e: Exception) {
            android.util.Log.e("SplitStayRepository", "Error adding roommate: ${e.message}")
        }
    }

    // Expenses
    fun getExpenses(roomId: String): Flow<List<Expense>> = expenseDao.getExpenses(roomId)

    suspend fun addExpense(expense: Expense) {
        try {
            ensureRoomExists(expense.roomId)
            expenseDao.insertExpense(expense)
            firestore?.collection("expenses")?.document(expense.id)?.set(expense)?.await()
        } catch (e: Exception) {
            android.util.Log.e("SplitStayRepository", "Error adding expense: ${e.message}")
        }
    }

    suspend fun updateExpense(expense: Expense) {
        try {
            ensureRoomExists(expense.roomId)
            expenseDao.updateExpense(expense)
            firestore?.collection("expenses")?.document(expense.id)?.set(expense)?.await()
        } catch (e: Exception) {
            android.util.Log.e("SplitStayRepository", "Error updating expense: ${e.message}")
        }
    }

    suspend fun deleteExpense(expense: Expense) {
        try {
            expenseDao.deleteExpense(expense)
            firestore?.collection("expenses")?.document(expense.id)?.delete()?.await()
        } catch (e: Exception) {
            android.util.Log.e("SplitStayRepository", "Error deleting expense: ${e.message}")
        }
    }

    // Chores
    fun getChores(roomId: String): Flow<List<Chore>> = choreDao.getChores(roomId)

    suspend fun addChore(chore: Chore) {
        try {
            ensureRoomExists(chore.roomId)
            choreDao.insertChore(chore)
            firestore?.collection("chores")?.document(chore.id)?.set(chore)?.await()
        } catch (e: Exception) {
            android.util.Log.e("SplitStayRepository", "Error adding chore: ${e.message}")
        }
    }

    suspend fun updateChore(chore: Chore) {
        try {
            ensureRoomExists(chore.roomId)
            choreDao.updateChore(chore)
            firestore?.collection("chores")?.document(chore.id)?.set(chore)?.await()
        } catch (e: Exception) {
            android.util.Log.e("SplitStayRepository", "Error updating chore: ${e.message}")
        }
    }

    // Settlements
    fun getSettlements(roomId: String): Flow<List<Settlement>> = settlementDao.getSettlements(roomId)

    suspend fun addSettlement(settlement: Settlement) {
        try {
            ensureRoomExists(settlement.roomId)
            settlementDao.insertSettlement(settlement)
            firestore?.collection("settlements")?.document(settlement.id)?.set(settlement)?.await()
        } catch (e: Exception) {
            android.util.Log.e("SplitStayRepository", "Error adding settlement: ${e.message}")
        }
    }

    // Sync from Firestore
    fun startSync(roomId: String) {
        firestore?.collection("expenses")?.whereEqualTo("roomId", roomId)
            ?.addSnapshotListener { snapshot, _ ->
                snapshot?.documents?.forEach { doc ->
                    // Simplified: map document to Expense and insert to Room
                    // In a real app, use a proper mapping and handle deletions
                }
            }
        // Repeat for other collections...
    }
}
