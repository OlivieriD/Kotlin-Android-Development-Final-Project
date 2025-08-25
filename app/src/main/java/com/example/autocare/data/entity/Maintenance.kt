package com.example.autocare.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "maintenance",
    foreignKeys = [
        ForeignKey(
            entity = Car::class,
            parentColumns = ["id"],
            childColumns = ["carId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Maintenance(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val carId: Int,
    val type: String, // Oil Change, Tire Rotation, General Service, Inspection
    val description: String,
    val scheduledDate: Long,
    val completedDate: Long? = null,
    val cost: Double = 0.0,
    val mileage: Int = 0,
    val mechanicName: String = "",
    val notes: String = "",
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)