package com.example.autocare.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.autocare.data.entity.Car
import com.example.autocare.data.entity.Maintenance
import com.example.autocare.data.entity.Expense
import com.example.autocare.data.entity.Mechanic

@Database(
    entities = [Car::class, Maintenance::class, Expense::class, Mechanic::class],
    version = 5,
    exportSchema = false
)
abstract class AutoCareDatabase : RoomDatabase() {
    abstract fun carDao(): CarDao
    abstract fun maintenanceDao(): MaintenanceDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun mechanicDao(): MechanicDao
    companion object {
        @Volatile
        private var INSTANCE: AutoCareDatabase? = null

        fun getDatabase(context: Context): AutoCareDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AutoCareDatabase::class.java,
                    "autocare_database"
                )
                    .fallbackToDestructiveMigration() // For development only
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}