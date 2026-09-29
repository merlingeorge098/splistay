package com.example.splistay.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.splistay.data.model.*

@Database(
    entities = [StayRoom::class, Roommate::class, Expense::class, Chore::class, Settlement::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stayRoomDao(): StayRoomDao
    abstract fun roommateDao(): RoommateDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun choreDao(): ChoreDao
    abstract fun settlementDao(): SettlementDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "splitstay_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
