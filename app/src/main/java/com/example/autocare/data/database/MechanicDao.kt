package com.example.autocare.data.database

import androidx.room.*
import com.example.autocare.data.entity.Mechanic
import kotlinx.coroutines.flow.Flow

@Dao
interface MechanicDao {
    @Query("SELECT * FROM mechanics WHERE carId = :carId ORDER BY name ASC")
    fun getMechanicsByCarId(carId: Int): Flow<List<Mechanic>>

    @Insert
    suspend fun insertMechanic(mechanic: Mechanic): Long

    @Update
    suspend fun updateMechanic(mechanic: Mechanic)

    @Delete
    suspend fun deleteMechanic(mechanic: Mechanic)

    @Query("SELECT * FROM mechanics WHERE id = :id")
    suspend fun getMechanicById(id: Int): Mechanic?
}