package com.example.expensehome.addexpense

import java.io.File

internal interface AddExpenseContract {
    data class ViewState(
        val title: String,
        val amount: String,
        val category: String,
        val date: Long,
        val paymentMethod: String,
        val notes: String,
        val isSaving: Boolean,
        val isSuggestionLoading: Boolean,
        val addExpenseType: AddExpenseType,
        val errorMessage: String?
    ){
        companion object {
            val Default = ViewState(
                title = "",
                amount = "",
                category = "",
                date = System.currentTimeMillis(),
                paymentMethod = "Cash",
                notes = "",
                isSaving = false,
                isSuggestionLoading = false,
                addExpenseType = AddExpenseType.DEFAULT,
                errorMessage = null
            )
        }
    }

    enum class AddExpenseType {
        DEFAULT,
        SCAN
    }

    sealed interface Event {
        data class TitleChanged(val title: String) : Event
        data class AmountChanged(val amount: String) : Event
        data class CategoryChanged(val category: String) : Event
        data class DateChanged(val date: Long) : Event
        data class PaymentMethodChanged(val method: String) : Event
        data class NotesChanged(val notes: String) : Event
        data class ImageCaptured(val file: File) : Event
        object SaveExpenseClicked : Event
        object AiCategorySearchClicked : Event
        object ScanReceiptClicked : Event
    }

    sealed interface Actions {
        data object NavigateToHome : Actions
    }
}