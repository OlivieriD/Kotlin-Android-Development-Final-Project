package com.example.autocare.data.database

import androidx.room.*
import com.example.autocare.data.entity.Car
import kotlinx.coroutines.flow.Flow

@Dao
interface CarDao {
    @Query("SELECT * FROM cars ORDER BY createdAt DESC")
    fun getAllCars(): Flow<List<Car>>

    @Query("SELECT * FROM cars WHERE id = :id")
    suspend fun getCarById(id: Int): Car?

    @Insert
    suspend fun insertCar(car: Car): Long

    @Update
    suspend fun updateCar(car: Car)

    @Delete
    suspend fun deleteCar(car: Car)

    @Query("DELETE FROM cars WHERE id = :id")
    suspend fun deleteCarById(id: Int)
}