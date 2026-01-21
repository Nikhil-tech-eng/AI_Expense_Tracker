package com.example.repository

import com.example.data.model.Expense
import com.example.data.remote.model.ExpenseExtractionResult
import com.example.data.remote.model.PromptType
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    suspend fun insertExpense(expense: Expense)
    fun getAllExpenses(): Flow<List<Expense>>
    suspend fun deleteExpense(expense: Expense)
    suspend fun getSuggestionFromGemini(title: String, promptType: PromptType) : String
    suspend fun extractExpenseDetailsFromGemini(text: String) : ExpenseExtractionResult
}