package com.example.autocare.data.repository

import com.example.autocare.data.database.MaintenanceDao
import com.example.autocare.data.entity.Maintenance
import kotlinx.coroutines.flow.Flow

class MaintenanceRepository(private val maintenanceDao: MaintenanceDao) {

    fun getMaintenanceByCarId(carId: Int): Flow<List<Maintenance>> =
        maintenanceDao.getMaintenanceByCarId(carId)

    suspend fun getMaintenanceById(id: Int): Maintenance? =
        maintenanceDao.getMaintenanceById(id)

    fun getOverdueMaintenance(currentDate: Long): Flow<List<Maintenance>> =
        maintenanceDao.getOverdueMaintenance(currentDate)

    fun getPendingMaintenance(): Flow<List<Maintenance>> =
        maintenanceDao.getPendingMaintenance()

    suspend fun insertMaintenance(maintenance: Maintenance): Long =
        maintenanceDao.insertMaintenance(maintenance)

    suspend fun updateMaintenance(maintenance: Maintenance) =
        maintenanceDao.updateMaintenance(maintenance)

    suspend fun deleteMaintenance(maintenance: Maintenance) =
        maintenanceDao.deleteMaintenance(maintenance)

    suspend fun markAsCompleted(id: Int, isCompleted: Boolean, completedDate: Long?) =
        maintenanceDao.markAsCompleted(id, isCompleted, completedDate)
}