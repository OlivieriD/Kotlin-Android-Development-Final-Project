package com.example.autocare.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "expenses",
    foreignKeys = [
        ForeignKey(
            entity = Car::class,
            parentColumns = ["id"],
            childColumns = ["carId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val carId: Int,
    val category: String, // Fuel, Maintenance, Insurance, Repairs, Registration, Other
    val amount: Double,
    val description: String,
    val date: Long,
    val location: String = "",
    val odometer: Int = 0, // Mileage at time of expense
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)