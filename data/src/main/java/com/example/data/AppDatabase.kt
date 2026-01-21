package com.example.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.local.dao.ExpenseDao
import com.example.data.model.Expense

@Database(entities = [Expense::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
}