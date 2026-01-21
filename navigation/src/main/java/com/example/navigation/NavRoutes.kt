package com.example.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoutes(val route: String) {
    @Serializable
    data object ExpenseList : NavRoutes("app/expense/list")

    @Serializable
    data object AddExpense : NavRoutes("app/expense/add")
}