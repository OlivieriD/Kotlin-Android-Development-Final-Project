package com.example.autocare.data.database

import androidx.room.*
import com.example.autocare.data.entity.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses WHERE carId = :carId ORDER BY date DESC")
    fun getExpensesByCarId(carId: Int): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE id = :id")
    suspend fun getExpenseById(id: Int): Expense?

    @Query("SELECT * FROM expenses WHERE carId = :carId AND category = :category ORDER BY date DESC")
    fun getExpensesByCategory(carId: Int, category: String): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE carId = :carId AND date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getExpensesByDateRange(carId: Int, startDate: Long, endDate: Long): Flow<List<Expense>>

    @Query("SELECT SUM(amount) FROM expenses WHERE carId = :carId")
    suspend fun getTotalExpensesByCarId(carId: Int): Double?

    @Query("SELECT SUM(amount) FROM expenses WHERE carId = :carId AND date BETWEEN :startDate AND :endDate")
    suspend fun getTotalExpensesByDateRange(carId: Int, startDate: Long, endDate: Long): Double?

    @Query("SELECT category, SUM(amount) as total FROM expenses WHERE carId = :carId GROUP BY category ORDER BY total DESC")
    suspend fun getExpensesByCategory(carId: Int): List<CategoryExpense>

    @Query("""
        SELECT strftime('%Y', date/1000, 'unixepoch') AS year, 
               SUM(amount) AS total 
        FROM expenses 
        WHERE carId = :carId 
        GROUP BY year 
        ORDER BY year DESC
    """)
    suspend fun getYearlyExpenses(carId: Int): List<YearlyExpense>

    @Insert
    suspend fun insertExpense(expense: Expense): Long

    @Update
    suspend fun updateExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)
}

data class CategoryExpense(
    val category: String,
    val total: Double
)

data class YearlyExpense(
    val year: String,
    val total: Double
)