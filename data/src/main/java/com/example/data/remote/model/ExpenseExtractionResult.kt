package com.example.data.remote.model

data class ExpenseExtractionResult(
    val title: String? = "NA",
    val amount: String? = "",
    val date: String? = System.currentTimeMillis().toString(),
    val category: String? = "NA"
)
