package com.example.expensehome.expenselist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Expense
import com.example.repository.ExpenseRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ExpenseListViewModel @Inject constructor(
    private val expenseRepositoryImpl: ExpenseRepositoryImpl
) : ViewModel() {

    val expenses = expenseRepositoryImpl.getAllExpenses().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            expenseRepositoryImpl.deleteExpense(expense)
        }
    }
}