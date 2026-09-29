package com.example.splistay.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(tableName = "stay_rooms")
data class StayRoom(
    @PrimaryKey val id: String,
    val roomName: String,
    val joinCode: String,
    val createdBy: String,
    val createdAt: Long,
    val pgLatitude: Double?,
    val pgLongitude: Double?
)

@Entity(
    tableName = "roommates",
    foreignKeys = [
        ForeignKey(
            entity = StayRoom::class,
            parentColumns = ["id"],
            childColumns = ["roomId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Roommate(
    @PrimaryKey val id: String,
    val roomId: String,
    val name: String,
    val avatarColor: Int,
    val joinedAt: Long,
    val reliabilityScore: Float = 100f
)

@Entity(
    tableName = "expenses",
    foreignKeys = [
        ForeignKey(
            entity = StayRoom::class,
            parentColumns = ["id"],
            childColumns = ["roomId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Expense(
    @PrimaryKey val id: String,
    val roomId: String,
    val description: String,
    val amount: Double,
    val category: String,
    val paidByRoommateId: String,
    val splitBetween: Map<String, Double>, // Map of roommate IDs to their share amount
    val splitType: String, // "equal" or "custom"
    val notes: String?,
    val receiptPhotoUri: String?,
    val date: Long,
    val syncStatus: Int = 0 // 0: Pending, 1: Synced
)

@Entity(
    tableName = "chores",
    foreignKeys = [
        ForeignKey(
            entity = StayRoom::class,
            parentColumns = ["id"],
            childColumns = ["roomId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Chore(
    @PrimaryKey val id: String,
    val roomId: String,
    val title: String,
    val assignedToRoommateId: String,
    val status: String, // "pending", "today", "completed"
    val dueDate: Long,
    val proofPhotoUri: String?,
    val proofLatitude: Double?,
    val proofLongitude: Double?,
    val proofTimestamp: Long?,
    val gpsVerified: Boolean = false
)

@Entity(
    tableName = "settlements",
    foreignKeys = [
        ForeignKey(
            entity = StayRoom::class,
            parentColumns = ["id"],
            childColumns = ["roomId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Settlement(
    @PrimaryKey val id: String,
    val roomId: String,
    val fromRoommateId: String,
    val toRoommateId: String,
    val amount: Double,
    val settledOn: Long,
    val verifiedViaApi: Boolean = false
)
