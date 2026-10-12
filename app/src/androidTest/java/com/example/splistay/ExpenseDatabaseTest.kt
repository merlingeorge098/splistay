package com.example.splistay

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.splistay.data.local.AppDatabase
import com.example.splistay.data.model.ExpenseEntity
import com.example.splistay.data.model.RoomEntity
import com.example.splistay.data.model.UserEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class ExpenseDatabaseTest {
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertMultipleExpenses_addsMultipleRows() = runBlocking {
        val roomId = "test_room"
        db.roomDao().insertRoom(RoomEntity(roomId, "Test Room", "Test Address", 0.0, 0.0))
        db.userDao().insertUser(UserEntity("user_1", roomId, "Alex", "1234567890", "alex@upi"))

        val expense1 = ExpenseEntity(
            expense_id = UUID.randomUUID().toString(),
            room_id = roomId,
            paid_by_user_id = "user_1",
            title = "Lunch",
            amount = 200.0,
            category = "Food",
            timestamp = System.currentTimeMillis(),
            is_synced = false
        )

        val expense2 = ExpenseEntity(
            expense_id = UUID.randomUUID().toString(),
            room_id = roomId,
            paid_by_user_id = "user_1",
            title = "Dinner",
            amount = 350.0,
            category = "Food",
            timestamp = System.currentTimeMillis(),
            is_synced = false
        )

        db.expenseDao().insertExpense(expense1)
        db.expenseDao().insertExpense(expense2)

        val expenses = db.expenseDao().getExpensesForRoom(roomId).first()
        assertEquals(2, expenses.size)
    }
}
