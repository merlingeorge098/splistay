package com.example.splistay.data.repository

import com.example.splistay.data.local.*
import com.example.splistay.data.model.*
import com.example.splistay.network.ApiService
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class SplitStayRepository(
    private val roomDao: RoomDao,
    private val userDao: UserDao,
    private val expenseDao: ExpenseDao,
    private val expenseSplitDao: ExpenseSplitDao,
    private val choreDao: ChoreDao,
    private val utilityPredictionDao: UtilityPredictionDao,
    private val alertDao: AlertDao,
    private val apiService: ApiService,
    private val firestore: FirebaseFirestore?
) {
    suspend fun ensureRoomAndUserExists(roomId: String) {
        try {
            // 1. Rooms
            roomDao.insertRoom(
                RoomEntity(
                    room_id = roomId,
                    room_name = "Sunny Meadows PG",
                    address = "123 Main St, Koramangala",
                    latitude = 12.9716,
                    longitude = 77.5946
                )
            )
            roomDao.insertRoom(
                RoomEntity(
                    room_id = "room_2",
                    room_name = "Greenwood Hostels",
                    address = "456 Park Avenue",
                    latitude = 12.9352,
                    longitude = 77.6245
                )
            )

            // 2. Users
            val user1 = UserEntity(user_id = "user_1", room_id = roomId, name = "Alex (You)", phone = "9876543210", upi_id = "alex@upi")
            val user2 = UserEntity(user_id = "user_2", room_id = roomId, name = "Rahul", phone = "9876543211", upi_id = "rahul@upi")
            val user3 = UserEntity(user_id = "user_3", room_id = roomId, name = "Priya", phone = "9876543212", upi_id = "priya@upi")
            val user4 = UserEntity(user_id = "user_4", room_id = "room_2", name = "Neha", phone = "9876543213", upi_id = "neha@upi")
            userDao.insertUsers(listOf(user1, user2, user3, user4))

            // 3. Expenses
            val exp1Id = "exp_1"
            val exp2Id = "exp_2"
            val exp3Id = "exp_3"

            expenseDao.insertExpense(
                ExpenseEntity(expense_id = exp1Id, room_id = roomId, paid_by_user_id = "user_1", title = "Weekly Groceries", amount = 1200.0, category = "Groceries", timestamp = System.currentTimeMillis() - 86400000L, is_synced = true)
            )
            expenseDao.insertExpense(
                ExpenseEntity(expense_id = exp2Id, room_id = roomId, paid_by_user_id = "user_2", title = "High-Speed Wi-Fi Bill", amount = 999.0, category = "Bills", timestamp = System.currentTimeMillis() - 43200000L, is_synced = true)
            )
            expenseDao.insertExpense(
                ExpenseEntity(expense_id = exp3Id, room_id = roomId, paid_by_user_id = "user_3", title = "Electricity Bill", amount = 2450.0, category = "Utilities", timestamp = System.currentTimeMillis(), is_synced = false)
            )

            // 4. Expense Splits
            expenseSplitDao.insertSplits(
                listOf(
                    ExpenseSplitEntity(expense_id = exp1Id, user_id = "user_1", amount_owed = 400.0, is_settled = true),
                    ExpenseSplitEntity(expense_id = exp1Id, user_id = "user_2", amount_owed = 400.0, is_settled = false),
                    ExpenseSplitEntity(expense_id = exp1Id, user_id = "user_3", amount_owed = 400.0, is_settled = false),
                    ExpenseSplitEntity(expense_id = exp2Id, user_id = "user_1", amount_owed = 333.0, is_settled = true),
                    ExpenseSplitEntity(expense_id = exp3Id, user_id = "user_2", amount_owed = 816.6, is_settled = false)
                )
            )

            // 5. Chores
            choreDao.insertChore(
                ChoreEntity(chore_id = "chore_1", room_id = roomId, assigned_user_id = "user_1", chore_name = "Clean Bathroom", due_date = System.currentTimeMillis() + 3600000L, status = "today", proof_photo_path = null, proof_latitude = null, proof_longitude = null, completed_at = null)
            )
            choreDao.insertChore(
                ChoreEntity(chore_id = "chore_2", room_id = roomId, assigned_user_id = "user_2", chore_name = "Take out Garbage", due_date = System.currentTimeMillis() - 3600000L, status = "pending", proof_photo_path = null, proof_latitude = null, proof_longitude = null, completed_at = null)
            )
            choreDao.insertChore(
                ChoreEntity(chore_id = "chore_3", room_id = roomId, assigned_user_id = "user_3", chore_name = "Mop Kitchen Floor", due_date = System.currentTimeMillis() + 86400000L, status = "completed", proof_photo_path = "storage/photos/kitchen.jpg", proof_latitude = 12.9716, proof_longitude = 77.5946, completed_at = System.currentTimeMillis())
            )

            // 6. Utility Predictions
            utilityPredictionDao.insertPredictions(
                listOf(
                    UtilityPredictionEntity(room_id = roomId, utility_type = "Electricity", predicted_amount = 2400.0, target_month = "November", suggested_advance_per_person = 800.0),
                    UtilityPredictionEntity(room_id = roomId, utility_type = "Water & Maintenance", predicted_amount = 1200.0, target_month = "November", suggested_advance_per_person = 400.0)
                )
            )

            // 7. Alerts
            alertDao.insertAlert(
                AlertEntity(room_id = roomId, user_id = "user_1", title = "Rent Due Reminder", message = "Monthly rent of ₹12,000 is due in 3 days.", trigger_timestamp = System.currentTimeMillis(), is_read = false)
            )
            alertDao.insertAlert(
                AlertEntity(room_id = roomId, user_id = "user_1", title = "Chore Overdue", message = "Rahul hasn't taken out the garbage yet.", trigger_timestamp = System.currentTimeMillis() - 7200000L, is_read = true)
            )

            android.util.Log.d("SplitStayRepository", "Successfully seeded all 7 tables with mock data.")
        } catch (e: Exception) {
            android.util.Log.e("SplitStayRepository", "Failed to seed database", e)
        }
    }

    // Rooms
    fun getRoom(roomId: String): Flow<RoomEntity?> = roomDao.getRoom(roomId)

    suspend fun createRoom(room: RoomEntity) {
        roomDao.insertRoom(room)
        try {
            firestore?.collection("rooms")?.document(room.room_id)?.set(room)?.await()
        } catch (e: Exception) {
            android.util.Log.e("SplitStayRepository", "Firestore room sync error", e)
        }
    }

    // Users (Roommates)
    fun getUsers(roomId: String): Flow<List<UserEntity>> = userDao.getUsersForRoom(roomId)
    fun getRoommates(roomId: String): Flow<List<UserEntity>> = userDao.getUsersForRoom(roomId)

    suspend fun addUser(user: UserEntity) {
        ensureRoomAndUserExists(user.room_id)
        userDao.insertUser(user)
        try {
            firestore?.collection("users")?.document(user.user_id)?.set(user)?.await()
        } catch (e: Exception) {
            android.util.Log.e("SplitStayRepository", "Firestore user sync error", e)
        }
    }

    suspend fun addRoommate(roommate: UserEntity) = addUser(roommate)

    // Expenses
    fun getExpenses(roomId: String): Flow<List<ExpenseEntity>> = expenseDao.getExpensesForRoom(roomId)

    suspend fun addExpense(expense: ExpenseEntity) {
        try {
            ensureRoomAndUserExists(expense.room_id)
            expenseDao.insertExpense(expense)
            
            // Insert default split entry
            expenseSplitDao.insertSplit(
                ExpenseSplitEntity(
                    expense_id = expense.expense_id,
                    user_id = expense.paid_by_user_id,
                    amount_owed = expense.amount,
                    is_settled = false
                )
            )
            android.util.Log.d("SplitStayRepository", "Successfully added expense: ${expense.title} amount: ${expense.amount}")

            try {
                firestore?.collection("expenses")?.document(expense.expense_id)?.set(expense)?.await()
            } catch (e: Exception) {
                android.util.Log.e("SplitStayRepository", "Firestore expense sync error", e)
            }
        } catch (e: Exception) {
            android.util.Log.e("SplitStayRepository", "FAILED to add expense to local DB", e)
        }
    }

    suspend fun updateExpense(expense: ExpenseEntity) {
        ensureRoomAndUserExists(expense.room_id)
        expenseDao.updateExpense(expense)
        try {
            firestore?.collection("expenses")?.document(expense.expense_id)?.set(expense)?.await()
        } catch (e: Exception) {
            android.util.Log.e("SplitStayRepository", "Firestore expense update sync error", e)
        }
    }

    suspend fun deleteExpense(expense: ExpenseEntity) {
        expenseDao.deleteExpense(expense)
        try {
            firestore?.collection("expenses")?.document(expense.expense_id)?.delete()?.await()
        } catch (e: Exception) {
            android.util.Log.e("SplitStayRepository", "Firestore expense delete sync error", e)
        }
    }

    // Chores
    fun getChores(roomId: String): Flow<List<ChoreEntity>> = choreDao.getChoresForRoom(roomId)

    suspend fun addChore(chore: ChoreEntity) {
        ensureRoomAndUserExists(chore.room_id)
        choreDao.insertChore(chore)
        try {
            firestore?.collection("chores")?.document(chore.chore_id)?.set(chore)?.await()
        } catch (e: Exception) {
            android.util.Log.e("SplitStayRepository", "Firestore chore sync error", e)
        }
    }

    suspend fun updateChore(chore: ChoreEntity) {
        ensureRoomAndUserExists(chore.room_id)
        choreDao.updateChore(chore)
        try {
            firestore?.collection("chores")?.document(chore.chore_id)?.set(chore)?.await()
        } catch (e: Exception) {
            android.util.Log.e("SplitStayRepository", "Firestore chore update sync error", e)
        }
    }

    // Predictions & Alerts
    fun getUtilityPredictions(roomId: String): Flow<List<UtilityPredictionEntity>> = utilityPredictionDao.getPredictionsForRoom(roomId)
    fun getAlerts(roomId: String): Flow<List<AlertEntity>> = alertDao.getAlertsForRoom(roomId)
}
