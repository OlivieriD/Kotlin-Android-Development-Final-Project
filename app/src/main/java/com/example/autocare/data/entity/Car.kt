package com.example.autocare.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cars")
data class Car(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val make: String,
    val model: String,
    val year: Int,
    val registration: String,
    val color: String = "",
    val mileage: Int = 0,
    val imageUri: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)