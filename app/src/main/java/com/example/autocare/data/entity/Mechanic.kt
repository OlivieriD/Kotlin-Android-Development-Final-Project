package com.example.autocare.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "mechanics",
    foreignKeys = [
        ForeignKey(
            entity = Car::class,
            parentColumns = ["id"],
            childColumns = ["carId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Mechanic(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val carId: Int,
    val name: String,
    val phone: String,
    val serviceCenter: String = "",
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)