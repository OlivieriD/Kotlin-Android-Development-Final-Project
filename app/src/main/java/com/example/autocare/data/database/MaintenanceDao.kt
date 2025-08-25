package com.example.autocare.data.database

import androidx.room.*
import com.example.autocare.data.entity.Maintenance
import kotlinx.coroutines.flow.Flow

@Dao
interface MaintenanceDao {
    @Query("SELECT * FROM maintenance WHERE carId = :carId ORDER BY scheduledDate DESC")
    fun getMaintenanceByCarId(carId: Int): Flow<List<Maintenance>>

    @Query("SELECT * FROM maintenance WHERE id = :id")
    suspend fun getMaintenanceById(id: Int): Maintenance?

    @Query("SELECT * FROM maintenance WHERE isCompleted = 0 AND scheduledDate <= :currentDate ORDER BY scheduledDate ASC")
    fun getOverdueMaintenance(currentDate: Long): Flow<List<Maintenance>>

    @Query("SELECT * FROM maintenance WHERE isCompleted = 0 ORDER BY scheduledDate ASC")
    fun getPendingMaintenance(): Flow<List<Maintenance>>

    @Insert
    suspend fun insertMaintenance(maintenance: Maintenance): Long

    @Update
    suspend fun updateMaintenance(maintenance: Maintenance)

    @Delete
    suspend fun deleteMaintenance(maintenance: Maintenance)

    @Query("UPDATE maintenance SET isCompleted = :isCompleted, completedDate = :completedDate WHERE id = :id")
    suspend fun markAsCompleted(id: Int, isCompleted: Boolean, completedDate: Long?)
}