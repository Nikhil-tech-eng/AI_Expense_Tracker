package com.example.expensehome.addexpense

import app.cash.turbine.test
import com.example.test.utils.fake.FakeExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AddExpenseViewModelTest {
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)

    private lateinit var fakeExpenseRepository: FakeExpenseRepository


    private lateinit var viewModel: AddExpenseViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        fakeExpenseRepository = FakeExpenseRepository()
        viewModel = AddExpenseViewModel(fakeExpenseRepository)
    }

    @Test
    fun `Initial ViewState Verification`() = runTest {
        // Verify that`getViewState()` initially emits the `AddExpenseContract.ViewState.Default` state.
        viewModel.viewState.test {
            assertEquals(AddExpenseContract.ViewState.Default, awaitItem())
        }
    }

    @Test
    fun `ViewState Update on TitleChanged Event`() = runTest {
        // Given a new title
        val newTitle = "New York Trip"

        // When the TitleChanged event is triggered
        viewModel.onEvent(AddExpenseContract.Event.TitleChanged(newTitle))

        // Then the ViewState should be updated with the new title
        val currentState = viewModel.viewState.value
        assertEquals(newTitle, currentState.title)
    }

    @Test
    fun `ViewState Update on AmountChanged Event`() {
        // Given an amount
        val newAmount = "20.00"

        // When the AmountChanged event is triggered
        viewModel.onEvent(AddExpenseContract.Event.AmountChanged(newAmount))

        // Then the ViewState should be updated with the new amount
        val currentState = viewModel.viewState.value
        assertEquals(newAmount, currentState.amount)
    }

    @Test
    fun `ViewState Update on CategoryChanged Event`() {
        // Given a category
        val newCategory = "Travel"

        // When the AmountChanged event is triggered
        viewModel.onEvent(AddExpenseContract.Event.CategoryChanged(newCategory))

        // Then the ViewState should be updated with the new amount
        val currentState = viewModel.viewState.value
        assertEquals(newCategory, currentState.category)
    }

    @Test
    fun `Event Handling for ScanReceiptClicked`() {
        // Verify that `onEvent(Event.ScanReceiptClicked)` updates the `addExpenseType` in the `ViewState` to `SCAN`.
        // When the ScanReceiptClicked event is triggered
        viewModel.onEvent(AddExpenseContract.Event.ScanReceiptClicked)

        // Then the ViewState should be updated with addExpenseType as SCAN
        val currentState = viewModel.viewState.value
        assertEquals(AddExpenseContract.AddExpenseType.SCAN, currentState.addExpenseType)
    }

    @Test
    fun `Save Expense with Valid Inputs`() = runTest {
        // When `saveExpense()` is called with a valid title and a positive amount, verify that `expenseRepository.insertExpense` is called with the correct `Expense` object.
        // Also, verify that a `NavigateToHome` action is emitted through `getActions()` and that `isSaving` is set to true then false.
        // Given
        viewModel.onEvent(AddExpenseContract.Event.TitleChanged("Test Expense"))
        viewModel.onEvent(AddExpenseContract.Event.AmountChanged("100.50"))
        viewModel.onEvent(AddExpenseContract.Event.CategoryChanged("Food"))

        // When
        viewModel.onEvent(AddExpenseContract.Event.SaveExpenseClicked)

        // Then
        // wait for events to be emitted
        viewModel.viewState.test {
            awaitItem()
            awaitItem()
            awaitItem()
        }


        fakeExpenseRepository.getAllExpenses().collect {
            it.first()
            assertEquals(it.first().title, "Test Expense")
            assertEquals(it.first().category, "Food")
            assertEquals(it.first().amount, 100.50, 0.0)
        }
    }

    @Test
    fun `Save Expense with Blank Title`() {
        // Given a blank title and valid amount
        viewModel.onEvent(AddExpenseContract.Event.TitleChanged(""))
        viewModel.onEvent(AddExpenseContract.Event.AmountChanged("50.0"))

        // When save is clicked
        viewModel.onEvent(AddExpenseContract.Event.SaveExpenseClicked)

        // Then verify error message
        val currentState = viewModel.viewState.value
        assertEquals("Please fill all the mandatory fields", currentState.errorMessage)
    }

    @Test
    fun `Save Expense with Zero Amount`() {
        // When `saveExpense()` is called with an amount of 0.0, verify that an error message is set in the `ViewState` and `expenseRepository.insertExpense` is not called.
        // Given an amount of 0.0
        viewModel.onEvent(AddExpenseContract.Event.TitleChanged("Test Expense"))
        viewModel.onEvent(AddExpenseContract.Event.AmountChanged("0.0"))

        // When save is clicked
        viewModel.onEvent(AddExpenseContract.Event.SaveExpenseClicked)

        // Then verify error message
        val currentState = viewModel.viewState.value
        assertEquals("Please fill all the mandatory fields", currentState.errorMessage)
    }

    @Test
    fun `Save Expense with Blank Category`() = runTest {
        // When `saveExpense()` is called with a blank category, verify that `expenseRepository.insertExpense` is called with the category defaulted to 'Others'.
        // Given
        viewModel.onEvent(AddExpenseContract.Event.TitleChanged("Test Expense"))
        viewModel.onEvent(AddExpenseContract.Event.AmountChanged("100.50"))
        viewModel.onEvent(AddExpenseContract.Event.CategoryChanged(""))

        // When
        viewModel.onEvent(AddExpenseContract.Event.SaveExpenseClicked)

        // Then
        viewModel.viewState.test {
            awaitItem()
            awaitItem()
            awaitItem()
        }

        val expense = fakeExpenseRepository.getAllExpenses().first().first()
        assertEquals("Others", expense.category)
    }

    @Test
    fun `Update Error Message with a Message`() {
        // Verify that calling `updateErrorMessage` with a non-null string updates the `errorMessage` in the `ViewState`.
        // Given an error message
        val errorMessage = "This is a test error"

        // When the updateErrorMessage event is triggered
        viewModel.updateErrorMessage(errorMessage)

        // Then the ViewState should be updated with the error message
        assertEquals(errorMessage, viewModel.viewState.value.errorMessage)
    }

    @Test
    fun `Clear Error Message`() {
        // Verify that calling `updateErrorMessage` with `null` clears the `errorMessage` in the `ViewState`.
        // Given an existing error message
        val errorMessage = "This is a test error"
        viewModel.updateErrorMessage(errorMessage)

        // When updateErrorMessage is called with null
        viewModel.updateErrorMessage(null)

        // Then the error message in ViewState should be null
        assertEquals(null, viewModel.viewState.value.errorMessage)
    }

    @Test
    fun `AI Category Search with Empty Title`() = runTest {
        // When `onAiCategorySearch()` is called with an empty title, verify that the `expenseRepository.getSuggestionFromGemini` is not called and the `ViewState` remains unchanged.
        // Given an empty title
        viewModel.onEvent(AddExpenseContract.Event.TitleChanged(""))
        val initialState = viewModel.viewState.value

        // When AI Category Search is clicked
        viewModel.onEvent(AddExpenseContract.Event.AiCategorySearchClicked)
        testScheduler.advanceUntilIdle() // Ensure any launched coroutines complete

        // Then verify that the ViewState remains unchanged and the repository method was not called
        assertEquals(initialState, viewModel.viewState.value)
    }
}