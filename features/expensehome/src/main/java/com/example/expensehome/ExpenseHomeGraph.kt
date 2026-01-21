package com.example.expensehome

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.expensehome.addexpense.ui.AddExpenseScreen
import com.example.expensehome.expenselist.ui.ExpenseListScreen
import com.example.navigation.NavRoutes


fun NavGraphBuilder.expenseHomeGraph(navHostController: NavHostController) {
    composable(
        route = NavRoutes.ExpenseList.route
    ) {
        ExpenseListScreen(
            onAddClick = { navHostController.navigateToAddExpense() }
        )
    }

    composable(
        route = NavRoutes.AddExpense.route
    ) {
        AddExpenseScreen(navigateToHome = { navHostController.popBackStack() })
    }
}

fun NavController.navigateToAddExpense() {
    navigate(NavRoutes.AddExpense.route)
}