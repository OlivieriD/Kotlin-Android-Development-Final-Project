package com.example.autocare.data.repository

import com.example.autocare.data.database.CarDao
import com.example.autocare.data.entity.Car
import kotlinx.coroutines.flow.Flow

class CarRepository(private val carDao: CarDao) {

    fun getAllCars(): Flow<List<Car>> = carDao.getAllCars()

    suspend fun getCarById(id: Int): Car? = carDao.getCarById(id)

    suspend fun insertCar(car: Car): Long = carDao.insertCar(car)

    suspend fun updateCar(car: Car) = carDao.updateCar(car)

    suspend fun deleteCar(car: Car) = carDao.deleteCar(car)

    suspend fun deleteCarById(id: Int) = carDao.deleteCarById(id)
}