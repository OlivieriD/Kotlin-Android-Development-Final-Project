package com.example.autocare.data.repository

import com.example.autocare.data.database.CategoryExpense
import com.example.autocare.data.database.ExpenseDao
import com.example.autocare.data.database.YearlyExpense
import com.example.autocare.data.entity.Expense
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(private val expenseDao: ExpenseDao) {

    fun getExpensesByCarId(carId: Int): Flow<List<Expense>> =
        expenseDao.getExpensesByCarId(carId)

    suspend fun getExpenseById(id: Int): Expense? =
        expenseDao.getExpenseById(id)

    fun getExpensesByCategory(carId: Int, category: String): Flow<List<Expense>> =
        expenseDao.getExpensesByCategory(carId, category)

    fun getExpensesByDateRange(carId: Int, startDate: Long, endDate: Long): Flow<List<Expense>> =
        expenseDao.getExpensesByDateRange(carId, startDate, endDate)

    suspend fun getTotalExpensesByCarId(carId: Int): Double =
        expenseDao.getTotalExpensesByCarId(carId) ?: 0.0

    suspend fun getTotalExpensesByDateRange(carId: Int, startDate: Long, endDate: Long): Double =
        expenseDao.getTotalExpensesByDateRange(carId, startDate, endDate) ?: 0.0

    suspend fun getExpensesByCategory(carId: Int): List<CategoryExpense> =
        expenseDao.getExpensesByCategory(carId)

    suspend fun insertExpense(expense: Expense): Long =
        expenseDao.insertExpense(expense)

    suspend fun updateExpense(expense: Expense) =
        expenseDao.updateExpense(expense)

    suspend fun deleteExpense(expense: Expense) =
        expenseDao.deleteExpense(expense)
    suspend fun getYearlyExpenses(carId: Int): List<YearlyExpense> =
        expenseDao.getYearlyExpenses(carId)
}