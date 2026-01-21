package com.example.expensehome.addexpense.ui

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.example.camerax.ReceiptScannerScreen
import com.example.data.utils.formatDateFromMillis
import com.example.expensehome.addexpense.AddExpenseContract
import com.example.expensehome.addexpense.AddExpenseViewModel
import java.util.Calendar

@Composable
internal fun AddExpenseScreen(
    viewModel: AddExpenseViewModel = hiltViewModel(),
    navigateToHome: () -> Unit
) {
    fun handleActions(action: AddExpenseContract.Actions) {
        when (action) {
            AddExpenseContract.Actions.NavigateToHome -> navigateToHome()
        }
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(viewModel.actions) {
        viewModel.actions.flowWithLifecycle(
            lifecycle = lifecycle,
            Lifecycle.State.RESUMED
        ).collect(::handleActions)
    }
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    when (viewState.addExpenseType) {
        AddExpenseContract.AddExpenseType.DEFAULT -> {
            AddExpenseHome(
                viewState = viewState,
                onSave = { viewModel.onEvent(AddExpenseContract.Event.SaveExpenseClicked) },
                onTitleChange = { viewModel.onEvent(AddExpenseContract.Event.TitleChanged(it)) },
                onAmountChange = { viewModel.onEvent(AddExpenseContract.Event.AmountChanged(it)) },
                onCategoryChange = { viewModel.onEvent(AddExpenseContract.Event.CategoryChanged(it)) },
                onDateChange = { viewModel.onEvent(AddExpenseContract.Event.DateChanged(it)) },
                onPaymentMethodChange = { viewModel.onEvent(AddExpenseContract.Event.PaymentMethodChanged(it)) },
                onNotesChange = { viewModel.onEvent(AddExpenseContract.Event.NotesChanged(it)) },
                onAiCategorySearch = { viewModel.onEvent(AddExpenseContract.Event.AiCategorySearchClicked) },
                onScanReceipt = { viewModel.onEvent(AddExpenseContract.Event.ScanReceiptClicked) },
                onBackClick = navigateToHome
            )
        }

        AddExpenseContract.AddExpenseType.SCAN -> {
            ReceiptScannerScreen(onImageCaptured = { viewModel.onImageCaptured(it) })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddExpenseHome(
    viewState: AddExpenseContract.ViewState,
    onSave: () -> Unit,
    onTitleChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onDateChange: (Long) -> Unit,
    onPaymentMethodChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onAiCategorySearch: () -> Unit,
    onScanReceipt: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val categories = listOf(
        "Food & Drinks", "Travel", "Shopping", "Bills", "Entertainment",
        "Health", "Education", "Groceries", "Personal", "Other"
    )
    val paymentMethods = listOf("Cash", "UPI", "Debit Card", "Credit Card", "Bank Transfer", "Other")

    var categoryDropdownExpanded by remember { mutableStateOf(false) }
    var paymentDropdownExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Add Transaction",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Transaction Details",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        // Title
                        OutlinedTextField(
                            value = viewState.title,
                            onValueChange = onTitleChange,
                            label = { Text("Expense Title *") },
                            leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        // Amount
                        OutlinedTextField(
                            value = viewState.amount,
                            onValueChange = onAmountChange,
                            label = { Text("Amount (₹) *") },
                            leadingIcon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        // Category Dropdown
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = viewState.category,
                                onValueChange = {},
                                label = { Text("Category *") },
                                leadingIcon = { Icon(Icons.Default.List, contentDescription = null) },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = null,
                                        modifier = Modifier.clickable { categoryDropdownExpanded = true }
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = true
                            )
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .clickable { categoryDropdownExpanded = true }
                            )
                            DropdownMenu(
                                expanded = categoryDropdownExpanded,
                                onDismissRequest = { categoryDropdownExpanded = false },
                                modifier = Modifier.fillMaxWidth(0.9f)
                            ) {
                                categories.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(item) },
                                        onClick = {
                                            onCategoryChange(item)
                                            categoryDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // AI Helper Button Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            AiSuggestButton(
                                onClick = onAiCategorySearch,
                                isLoading = viewState.isSuggestionLoading,
                                modifier = Modifier.weight(1f)
                            )

                            OutlinedButton(
                                onClick = onScanReceipt,
                                modifier = Modifier.weight(1f),
                                contentPadding = ButtonDefaults.ContentPadding
                            ) {
                                Icon(Icons.Default.List, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Scan Receipt", fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Additional Information",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        // Date Picker trigger textfield
                        val calendar = Calendar.getInstance().apply { timeInMillis = viewState.date }
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = formatDateFromMillis(viewState.date),
                                onValueChange = {},
                                label = { Text("Date *") },
                                leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = true
                            )
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .clickable {
                                        DatePickerDialog(
                                            context,
                                            { _, year, month, dayOfMonth ->
                                                val selCal = Calendar.getInstance()
                                                selCal.set(year, month, dayOfMonth)
                                                onDateChange(selCal.timeInMillis)
                                            },
                                            calendar.get(Calendar.YEAR),
                                            calendar.get(Calendar.MONTH),
                                            calendar.get(Calendar.DAY_OF_MONTH)
                                        ).show()
                                    }
                            )
                        }

                        // Payment Method Dropdown
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = viewState.paymentMethod,
                                onValueChange = {},
                                label = { Text("Payment Method") },
                                leadingIcon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = null,
                                        modifier = Modifier.clickable { paymentDropdownExpanded = true }
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = true
                            )
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .clickable { paymentDropdownExpanded = true }
                            )
                            DropdownMenu(
                                expanded = paymentDropdownExpanded,
                                onDismissRequest = { paymentDropdownExpanded = false },
                                modifier = Modifier.fillMaxWidth(0.9f)
                            ) {
                                paymentMethods.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(item) },
                                        onClick = {
                                            onPaymentMethodChange(item)
                                            paymentDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Notes field
                        OutlinedTextField(
                            value = viewState.notes,
                            onValueChange = onNotesChange,
                            label = { Text("Notes (Optional)") },
                            leadingIcon = { Icon(Icons.Default.Info, contentDescription = null) },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                viewState.errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Button(
                    onClick = onSave,
                    enabled = !viewState.isSaving,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Save Expense", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                }

                Spacer(modifier = Modifier.height(32.dp))
            }

            if (viewState.isSuggestionLoading) {
                AiPulseLoader()
            }
        }
    }
}
