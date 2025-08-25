package com.example.autocare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.autocare.data.entity.Car
import com.example.autocare.data.repository.CarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CarViewModel(private val repository: CarRepository) : ViewModel() {

    private val _cars = MutableStateFlow<List<Car>>(emptyList())
    val cars: StateFlow<List<Car>> = _cars.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _selectedCar = MutableStateFlow<Car?>(null)
    val selectedCar: StateFlow<Car?> = _selectedCar.asStateFlow()

    init {
        loadCars()
    }

    private fun loadCars() {
        viewModelScope.launch {
            repository.getAllCars().collect { carList ->
                _cars.value = carList
            }
        }
    }

    fun insertCar(car: Car) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.insertCar(car)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateCar(car: Car) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.updateCar(car)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteCar(car: Car) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.deleteCar(car)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getCarById(id: Int) {
        viewModelScope.launch {
            _selectedCar.value = repository.getCarById(id)
        }
    }

    fun clearSelectedCar() {
        _selectedCar.value = null
    }
}