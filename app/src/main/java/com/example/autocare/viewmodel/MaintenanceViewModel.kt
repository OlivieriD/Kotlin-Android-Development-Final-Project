package com.example.autocare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.autocare.data.entity.Maintenance
import com.example.autocare.data.repository.MaintenanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MaintenanceViewModel(private val repository: MaintenanceRepository) : ViewModel() {

    private val _maintenanceList = MutableStateFlow<List<Maintenance>>(emptyList())
    val maintenanceList: StateFlow<List<Maintenance>> = _maintenanceList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _selectedMaintenance = MutableStateFlow<Maintenance?>(null)
    val selectedMaintenance: StateFlow<Maintenance?> = _selectedMaintenance.asStateFlow()

    fun loadMaintenanceByCarId(carId: Int) {
        viewModelScope.launch {
            repository.getMaintenanceByCarId(carId).collect { maintenanceList ->
                _maintenanceList.value = maintenanceList
            }
        }
    }

    fun insertMaintenance(maintenance: Maintenance) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.insertMaintenance(maintenance)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateMaintenance(maintenance: Maintenance) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.updateMaintenance(maintenance)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteMaintenance(maintenance: Maintenance) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.deleteMaintenance(maintenance)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun markAsCompleted(id: Int, isCompleted: Boolean) {
        viewModelScope.launch {
            val completedDate = if (isCompleted) System.currentTimeMillis() else null
            repository.markAsCompleted(id, isCompleted, completedDate)
        }
    }

    fun getMaintenanceById(id: Int) {
        viewModelScope.launch {
            _selectedMaintenance.value = repository.getMaintenanceById(id)
        }
    }
}