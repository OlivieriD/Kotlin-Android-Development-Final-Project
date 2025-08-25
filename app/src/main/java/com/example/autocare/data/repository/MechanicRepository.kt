package com.example.autocare.data.repository

import com.example.autocare.data.database.MechanicDao
import com.example.autocare.data.entity.Mechanic
import kotlinx.coroutines.flow.Flow

class MechanicRepository(private val mechanicDao: MechanicDao) {
    fun getMechanicsByCarId(carId: Int): Flow<List<Mechanic>> =
        mechanicDao.getMechanicsByCarId(carId)

    suspend fun insertMechanic(mechanic: Mechanic): Long =
        mechanicDao.insertMechanic(mechanic)

    suspend fun updateMechanic(mechanic: Mechanic) =
        mechanicDao.updateMechanic(mechanic)

    suspend fun deleteMechanic(mechanic: Mechanic) =
        mechanicDao.deleteMechanic(mechanic)

    suspend fun getMechanicById(id: Int): Mechanic? =
        mechanicDao.getMechanicById(id)
}