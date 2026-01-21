package com.example.test.utils.fake

import com.example.data.model.Expense
import com.example.data.remote.model.ExpenseExtractionResult
import com.example.data.remote.model.PromptType
import com.example.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeExpenseRepository : ExpenseRepository {
    var expenses = mutableListOf<Expense>()

    override suspend fun insertExpense(expense: Expense) {
        expenses.add(expense)
    }

    override fun getAllExpenses(): Flow<List<Expense>> {
        return flowOf(expenses)
    }

    override suspend fun deleteExpense(expense: Expense) {
        expenses.remove(expense)
    }

    override suspend fun getSuggestionFromGemini(
        title: String,
        promptType: PromptType
    ): String {
        return "Other"
    }

    override suspend fun extractExpenseDetailsFromGemini(text: String): ExpenseExtractionResult {
        return ExpenseExtractionResult(title = "Dummy", amount = "100", category = "Other")
    }

    companion object {
        val dummyExpense = Expense(
            title = "Dummy Expense",
            amount = 100.0,
            date = 121994,
            category = "Dummy Category",
            id = "1",
            paymentMethod = "Cash",
            notes = ""
        )
    }

}