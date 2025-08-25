package com.example.autocare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.autocare.data.entity.Mechanic
import com.example.autocare.data.repository.MechanicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MechanicViewModel(private val repository: MechanicRepository) : ViewModel() {

    private val _mechanics = MutableStateFlow<List<Mechanic>>(emptyList())
    val mechanics: StateFlow<List<Mechanic>> = _mechanics.asStateFlow()

    private val _selectedMechanic = MutableStateFlow<Mechanic?>(null)
    val selectedMechanic: StateFlow<Mechanic?> = _selectedMechanic.asStateFlow()

    fun loadMechanicsByCarId(carId: Int) {
        viewModelScope.launch {
            repository.getMechanicsByCarId(carId).collect { mechanics ->
                _mechanics.value = mechanics
            }
        }
    }

    fun insertMechanic(mechanic: Mechanic) {
        viewModelScope.launch {
            repository.insertMechanic(mechanic)
        }
    }

    fun updateMechanic(mechanic: Mechanic) {
        viewModelScope.launch {
            repository.updateMechanic(mechanic)
        }
    }

    fun deleteMechanic(mechanic: Mechanic) {
        viewModelScope.launch {
            repository.deleteMechanic(mechanic)
        }
    }

    fun getMechanicById(id: Int) {
        viewModelScope.launch {
            _selectedMechanic.value = repository.getMechanicById(id)
        }
    }
}