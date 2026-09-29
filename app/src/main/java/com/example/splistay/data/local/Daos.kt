package com.example.splistay.data.local

import androidx.room.*
import com.example.splistay.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface StayRoomDao {
    @Query("SELECT * FROM stay_rooms WHERE id = :roomId")
    fun getRoom(roomId: String): Flow<StayRoom?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoom(room: StayRoom)
}

@Dao
interface RoommateDao {
    @Query("SELECT * FROM roommates WHERE roomId = :roomId")
    fun getRoommates(roomId: String): Flow<List<Roommate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoommate(roommate: Roommate)
}

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses WHERE roomId = :roomId ORDER BY date DESC")
    fun getExpenses(roomId: String): Flow<List<Expense>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    @Update
    suspend fun updateExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("DELETE FROM expenses WHERE id = :expenseId")
    suspend fun deleteExpenseById(expenseId: String)
}

@Dao
interface ChoreDao {
    @Query("SELECT * FROM chores WHERE roomId = :roomId ORDER BY dueDate ASC")
    fun getChores(roomId: String): Flow<List<Chore>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChore(chore: Chore)

    @Update
    suspend fun updateChore(chore: Chore)
}

@Dao
interface SettlementDao {
    @Query("SELECT * FROM settlements WHERE roomId = :roomId ORDER BY settledOn DESC")
    fun getSettlements(roomId: String): Flow<List<Settlement>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettlement(settlement: Settlement)
}
