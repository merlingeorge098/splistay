package com.example.splistay.data.local

import androidx.room.*
import com.example.splistay.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {
    @Query("SELECT * FROM rooms WHERE room_id = :roomId")
    fun getRoom(roomId: String): Flow<RoomEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoom(room: RoomEntity)

    @Update
    suspend fun updateRoom(room: RoomEntity)

    @Delete
    suspend fun deleteRoom(room: RoomEntity)
}

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE room_id = :roomId")
    fun getUsersForRoom(roomId: String): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE user_id = :userId")
    fun getUser(userId: String): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)
}

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses WHERE room_id = :roomId ORDER BY timestamp DESC")
    fun getExpensesForRoom(roomId: String): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE expense_id = :expenseId")
    fun getExpenseById(expenseId: String): Flow<ExpenseEntity?>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertExpense(expense: ExpenseEntity)

    @Update
    suspend fun updateExpense(expense: ExpenseEntity)

    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)

    @Query("DELETE FROM expenses WHERE expense_id = :expenseId")
    suspend fun deleteExpenseById(expenseId: String)

    @Query("SELECT * FROM expenses WHERE is_synced = 0")
    fun getUnsyncedExpenses(): Flow<List<ExpenseEntity>>
}

@Dao
interface ExpenseSplitDao {
    @Query("SELECT * FROM expense_splits WHERE expense_id = :expenseId")
    fun getSplitsForExpense(expenseId: String): Flow<List<ExpenseSplitEntity>>

    @Query("SELECT * FROM expense_splits WHERE user_id = :userId")
    fun getSplitsForUser(userId: String): Flow<List<ExpenseSplitEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSplit(split: ExpenseSplitEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSplits(splits: List<ExpenseSplitEntity>)

    @Update
    suspend fun updateSplit(split: ExpenseSplitEntity)

    @Delete
    suspend fun deleteSplit(split: ExpenseSplitEntity)
}

@Dao
interface ChoreDao {
    @Query("SELECT * FROM chores WHERE room_id = :roomId ORDER BY due_date ASC")
    fun getChoresForRoom(roomId: String): Flow<List<ChoreEntity>>

    @Query("SELECT * FROM chores WHERE assigned_user_id = :userId AND room_id = :roomId ORDER BY due_date ASC")
    fun getChoresForUser(userId: String, roomId: String): Flow<List<ChoreEntity>>

    @Query("SELECT * FROM chores WHERE chore_id = :choreId")
    fun getChoreById(choreId: String): Flow<ChoreEntity?>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertChore(chore: ChoreEntity)

    @Update
    suspend fun updateChore(chore: ChoreEntity)

    @Delete
    suspend fun deleteChore(chore: ChoreEntity)

    @Query("DELETE FROM chores WHERE chore_id = :choreId")
    suspend fun deleteChoreById(choreId: String)
}

@Dao
interface UtilityPredictionDao {
    @Query("SELECT * FROM utility_predictions WHERE room_id = :roomId")
    fun getPredictionsForRoom(roomId: String): Flow<List<UtilityPredictionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrediction(prediction: UtilityPredictionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPredictions(predictions: List<UtilityPredictionEntity>)

    @Delete
    suspend fun deletePrediction(prediction: UtilityPredictionEntity)
}

@Dao
interface AlertDao {
    @Query("SELECT * FROM alerts WHERE room_id = :roomId ORDER BY trigger_timestamp DESC")
    fun getAlertsForRoom(roomId: String): Flow<List<AlertEntity>>

    @Query("SELECT * FROM alerts WHERE user_id = :userId ORDER BY trigger_timestamp DESC")
    fun getAlertsForUser(userId: String): Flow<List<AlertEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: AlertEntity)

    @Update
    suspend fun updateAlert(alert: AlertEntity)

    @Delete
    suspend fun deleteAlert(alert: AlertEntity)
}
