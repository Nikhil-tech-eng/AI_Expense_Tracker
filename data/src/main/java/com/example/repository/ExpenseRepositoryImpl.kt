package com.example.repository

import com.example.data.local.dao.ExpenseDao
import com.example.data.model.Expense
import com.example.data.remote.model.PromptType
import com.example.data.remote.GeminiService
import com.example.data.remote.model.ExpenseExtractionResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpenseRepositoryImpl @Inject constructor(
    private val expenseDao: ExpenseDao,
    private val geminiService: GeminiService
) : ExpenseRepository {
    override fun getAllExpenses(): Flow<List<Expense>> =
        expenseDao.getAllExpenses()

    override suspend fun insertExpense(expense: Expense) {
        expenseDao.insertExpense(expense)
    }

    override suspend fun deleteExpense(expense: Expense) {
        expenseDao.deleteExpense(expense)
    }

    override suspend fun getSuggestionFromGemini(title: String, promptType: PromptType): String {
        return geminiService.generateContent(title, promptType)
    }

    override suspend fun extractExpenseDetailsFromGemini(text: String): ExpenseExtractionResult {
        return geminiService.extractExpenseDetails(text)
    }
}