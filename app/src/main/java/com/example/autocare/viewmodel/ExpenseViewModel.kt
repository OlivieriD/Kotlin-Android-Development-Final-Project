package com.example.autocare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.autocare.data.database.CategoryExpense
import com.example.autocare.data.database.YearlyExpense
import com.example.autocare.data.entity.Expense
import com.example.autocare.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExpenseViewModel(private val repository: ExpenseRepository) : ViewModel() {

    private val _expenseList = MutableStateFlow<List<Expense>>(emptyList())
    val expenseList: StateFlow<List<Expense>> = _expenseList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _selectedExpense = MutableStateFlow<Expense?>(null)
    val selectedExpense: StateFlow<Expense?> = _selectedExpense.asStateFlow()

    private val _totalExpenses = MutableStateFlow(0.0)
    val totalExpenses: StateFlow<Double> = _totalExpenses.asStateFlow()

    private val _categoryExpenses = MutableStateFlow<List<CategoryExpense>>(emptyList())
    val categoryExpenses: StateFlow<List<CategoryExpense>> = _categoryExpenses.asStateFlow()


    private val _yearlyExpenses = MutableStateFlow<List<YearlyExpense>>(emptyList())
    val yearlyExpenses: StateFlow<List<YearlyExpense>> = _yearlyExpenses.asStateFlow()

    fun loadExpensesByCarId(carId: Int) {
        viewModelScope.launch {
            _yearlyExpenses.value = repository.getYearlyExpenses(carId)
            repository.getExpensesByCarId(carId).collect { expenseList ->
                _expenseList.value = expenseList
            }
        }

        // Load total expenses
        viewModelScope.launch {
            _totalExpenses.value = repository.getTotalExpensesByCarId(carId)
        }

        // Load category breakdown
        viewModelScope.launch {
            _categoryExpenses.value = repository.getExpensesByCategory(carId)
        }
    }

    fun insertExpense(expense: Expense) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.insertExpense(expense)
                // Refresh totals
                _totalExpenses.value = repository.getTotalExpensesByCarId(expense.carId)
                _categoryExpenses.value = repository.getExpensesByCategory(expense.carId)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateExpense(expense: Expense) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.updateExpense(expense)
                // Refresh totals
                _totalExpenses.value = repository.getTotalExpensesByCarId(expense.carId)
                _categoryExpenses.value = repository.getExpensesByCategory(expense.carId)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.deleteExpense(expense)
                // Refresh totals
                _totalExpenses.value = repository.getTotalExpensesByCarId(expense.carId)
                _categoryExpenses.value = repository.getExpensesByCategory(expense.carId)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getExpenseById(id: Int) {
        viewModelScope.launch {
            _selectedExpense.value = repository.getExpenseById(id)
        }
    }

    fun getTotalExpensesByDateRange(carId: Int, startDate: Long, endDate: Long) {
        viewModelScope.launch {
            val total = repository.getTotalExpensesByDateRange(carId, startDate, endDate)
            // You can expose this through another StateFlow if needed
        }
    }
}