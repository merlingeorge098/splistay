package com.example.splistay.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.ColumnInfo

@Entity(tableName = "rooms")
data class RoomEntity(
    @PrimaryKey @ColumnInfo(name = "room_id") val room_id: String,
    @ColumnInfo(name = "room_name") val room_name: String,
    @ColumnInfo(name = "address") val address: String? = null,
    @ColumnInfo(name = "latitude") val latitude: Double? = null,
    @ColumnInfo(name = "longitude") val longitude: Double? = null
) {
    val id: String get() = room_id
    val roomId: String get() = room_id
    val roomName: String get() = room_name
    val joinCode: String get() = "123456"
    val createdBy: String get() = "user_1"
    val createdAt: Long get() = System.currentTimeMillis()
    val pgLatitude: Double? get() = latitude
    val pgLongitude: Double? get() = longitude
}

@Entity(
    tableName = "users",
    foreignKeys = [
        ForeignKey(
            entity = RoomEntity::class,
            parentColumns = ["room_id"],
            childColumns = ["room_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserEntity(
    @PrimaryKey @ColumnInfo(name = "user_id") val user_id: String,
    @ColumnInfo(name = "room_id") val room_id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "phone") val phone: String? = null,
    @ColumnInfo(name = "upi_id") val upi_id: String? = null
) {
    val id: String get() = user_id
    val userId: String get() = user_id
    val roomId: String get() = room_id
    val upiId: String? get() = upi_id
    val avatarColor: Int get() = 0xFF6200EE.toInt()
    val joinedAt: Long get() = System.currentTimeMillis()
    val reliabilityScore: Float get() = 100f
}

@Entity(
    tableName = "expenses",
    foreignKeys = [
        ForeignKey(
            entity = RoomEntity::class,
            parentColumns = ["room_id"],
            childColumns = ["room_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["paid_by_user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ExpenseEntity(
    @PrimaryKey @ColumnInfo(name = "expense_id") val expense_id: String,
    @ColumnInfo(name = "room_id") val room_id: String,
    @ColumnInfo(name = "paid_by_user_id") val paid_by_user_id: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "amount") val amount: Double,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    @ColumnInfo(name = "is_synced") val is_synced: Boolean = false
) {
    val id: String get() = expense_id
    val expenseId: String get() = expense_id
    val roomId: String get() = room_id
    val paidByUser: String get() = paid_by_user_id
    val paidByRoommateId: String get() = paid_by_user_id
    val description: String get() = title
    val date: Long get() = timestamp
    val syncStatus: Int get() = if (is_synced) 1 else 0
    val splitType: String get() = "equal"
    val splitBetween: Map<String, Double> get() = emptyMap()
    val notes: String? get() = null
    val receiptPhotoUri: String? get() = null
}

@Entity(
    tableName = "expense_splits",
    foreignKeys = [
        ForeignKey(
            entity = ExpenseEntity::class,
            parentColumns = ["expense_id"],
            childColumns = ["expense_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ExpenseSplitEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "split_id") val split_id: Long = 0L,
    @ColumnInfo(name = "expense_id") val expense_id: String,
    @ColumnInfo(name = "user_id") val user_id: String,
    @ColumnInfo(name = "amount_owed") val amount_owed: Double,
    @ColumnInfo(name = "is_settled") val is_settled: Boolean = false
) {
    val id: Long get() = split_id
    val splitId: Long get() = split_id
    val expenseId: String get() = expense_id
    val userId: String get() = user_id
}

@Entity(
    tableName = "chores",
    foreignKeys = [
        ForeignKey(
            entity = RoomEntity::class,
            parentColumns = ["room_id"],
            childColumns = ["room_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["assigned_user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ChoreEntity(
    @PrimaryKey @ColumnInfo(name = "chore_id") val chore_id: String,
    @ColumnInfo(name = "room_id") val room_id: String,
    @ColumnInfo(name = "assigned_user_id") val assigned_user_id: String,
    @ColumnInfo(name = "chore_name") val chore_name: String,
    @ColumnInfo(name = "due_date") val due_date: Long,
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "proof_photo_path") val proof_photo_path: String? = null,
    @ColumnInfo(name = "proof_latitude") val proof_latitude: Double? = null,
    @ColumnInfo(name = "proof_longitude") val proof_longitude: Double? = null,
    @ColumnInfo(name = "completed_at") val completed_at: Long? = null
) {
    val id: String get() = chore_id
    val choreId: String get() = chore_id
    val roomId: String get() = room_id
    val assignedToRoommateId: String get() = assigned_user_id
    val title: String get() = chore_name
    val dueDate: Long get() = due_date
    val proofPhotoUri: String? get() = proof_photo_path
    val proofLatitude: Double? get() = proof_latitude
    val proofLongitude: Double? get() = proof_longitude
    val proofTimestamp: Long? get() = completed_at
    val gpsVerified: Boolean get() = proof_latitude != null && proof_longitude != null
}

@Entity(
    tableName = "utility_predictions",
    foreignKeys = [
        ForeignKey(
            entity = RoomEntity::class,
            parentColumns = ["room_id"],
            childColumns = ["room_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UtilityPredictionEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "prediction_id") val prediction_id: Long = 0L,
    @ColumnInfo(name = "room_id") val room_id: String,
    @ColumnInfo(name = "utility_type") val utility_type: String,
    @ColumnInfo(name = "predicted_amount") val predicted_amount: Double,
    @ColumnInfo(name = "target_month") val target_month: String,
    @ColumnInfo(name = "suggested_advance_per_person") val suggested_advance_per_person: Double
) {
    val id: Long get() = prediction_id
    val predictionId: Long get() = prediction_id
    val roomId: String get() = room_id
}

@Entity(
    tableName = "alerts",
    foreignKeys = [
        ForeignKey(
            entity = RoomEntity::class,
            parentColumns = ["room_id"],
            childColumns = ["room_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AlertEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "alert_id") val alert_id: Long = 0L,
    @ColumnInfo(name = "room_id") val room_id: String,
    @ColumnInfo(name = "user_id") val user_id: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "message") val message: String,
    @ColumnInfo(name = "trigger_timestamp") val trigger_timestamp: Long,
    @ColumnInfo(name = "is_read") val is_read: Boolean = false
) {
    val id: Long get() = alert_id
    val alertId: Long get() = alert_id
    val roomId: String get() = room_id
    val userId: String get() = user_id
}

typealias Chore = ChoreEntity
typealias Expense = ExpenseEntity
typealias StayRoom = RoomEntity
typealias Roommate = UserEntity
