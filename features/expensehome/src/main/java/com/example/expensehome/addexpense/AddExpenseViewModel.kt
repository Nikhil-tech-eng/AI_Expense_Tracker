package com.example.expensehome.addexpense

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Expense
import com.example.data.remote.model.PromptType
import com.example.mltoolkit.ReceiptTextExtractor
import com.example.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
internal class AddExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {
    private val mutableViewState = MutableStateFlow(AddExpenseContract.ViewState.Default)
    val viewState = mutableViewState.asStateFlow()

    private val mutableActionFlow = MutableSharedFlow<AddExpenseContract.Actions>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val actions = mutableActionFlow.asSharedFlow()

    fun onEvent(event: AddExpenseContract.Event) {
        when (event) {
            is AddExpenseContract.Event.TitleChanged -> {
                updateErrorMessage(null)
                mutableViewState.update { it.copy(title = event.title) }
            }

            is AddExpenseContract.Event.AmountChanged -> {
                updateErrorMessage(null)
                mutableViewState.update { it.copy(amount = event.amount) }
            }

            is AddExpenseContract.Event.CategoryChanged -> {
                updateErrorMessage(null)
                mutableViewState.update { it.copy(category = event.category) }
            }

            is AddExpenseContract.Event.DateChanged -> {
                updateErrorMessage(null)
                mutableViewState.update { it.copy(date = event.date) }
            }

            is AddExpenseContract.Event.PaymentMethodChanged -> {
                updateErrorMessage(null)
                mutableViewState.update { it.copy(paymentMethod = event.method) }
            }

            is AddExpenseContract.Event.NotesChanged -> {
                updateErrorMessage(null)
                mutableViewState.update { it.copy(notes = event.notes) }
            }

            is AddExpenseContract.Event.ImageCaptured -> onImageCaptured(event.file)
            AddExpenseContract.Event.SaveExpenseClicked -> saveExpense()
            AddExpenseContract.Event.AiCategorySearchClicked -> onAiCategorySearch()
            AddExpenseContract.Event.ScanReceiptClicked -> {
                mutableViewState.update { it.copy(addExpenseType = AddExpenseContract.AddExpenseType.SCAN) }
            }
        }
    }

    fun saveExpense() {
        val currentState = mutableViewState.value
        val amount = currentState.amount.toDoubleOrNull()
        if (currentState.title.isBlank() || amount == null || amount <= 0.0) {
            updateErrorMessage("Please fill all the mandatory fields with valid values")
            return
        }
        viewModelScope.launch {
            isSaving(true)
            withContext(Dispatchers.IO) {
                try {
                    val expense = Expense(
                        category = currentState.category.ifBlank { "Other" },
                        amount = amount,
                        title = currentState.title,
                        date = currentState.date,
                        paymentMethod = currentState.paymentMethod,
                        notes = currentState.notes
                    )
                    expenseRepository.insertExpense(expense)
                    mutableActionFlow.emit(AddExpenseContract.Actions.NavigateToHome)
                    isSaving(false)
                } catch (_: Exception) {
                    updateErrorMessage("Couldn't save, please try again later")
                }
            }
        }
    }

    fun updateErrorMessage(message: String? = null) {
        mutableViewState.update { it.copy(errorMessage = message) }
    }

    private fun isSaving(isSaving: Boolean) {
        mutableViewState.update { it.copy(isSaving = isSaving) }
    }

    private fun isSuggestionLoading(isLoading: Boolean) {
        mutableViewState.update { it.copy(isSuggestionLoading = isLoading) }
    }

    internal fun onAiCategorySearch() {
        val titleText = mutableViewState.value.title
        if (titleText.isEmpty()) return

        viewModelScope.launch {
            isSuggestionLoading(true)
            try {
                val suggestion = expenseRepository.getSuggestionFromGemini(
                    titleText,
                    promptType = PromptType.CATEGORY_SUGGESTION
                )
                mutableViewState.update { it.copy(category = suggestion) }
            } catch (_: Exception) {
                mutableViewState.update { it.copy(category = "Other") }
            } finally {
                isSuggestionLoading(false)
            }
        }
    }

    fun onImageCaptured(it: File) {
        viewModelScope.launch {
            val text = ReceiptTextExtractor.extractText(it)
            fillFromText(text)
            mutableViewState.update { it.copy(addExpenseType = AddExpenseContract.AddExpenseType.DEFAULT) }
        }
    }

    private fun fillFromText(text: String) {
        viewModelScope.launch {
            try {
                isSuggestionLoading(true)
                val response = expenseRepository.extractExpenseDetailsFromGemini(
                    text
                )
                mutableViewState.update {
                    it.copy(
                        amount = response.amount ?: "",
                        category = response.category ?: "",
                        title = response.title ?: ""
                    )
                }
            } catch (e: Exception) {
                Log.e("AddExpenseViewModel", "fillFromText: ", e)
            } finally {
                isSuggestionLoading(false)
            }
        }
    }
}
