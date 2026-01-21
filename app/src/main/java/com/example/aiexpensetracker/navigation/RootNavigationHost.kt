package com.example.aiexpensetracker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.expensehome.expenseHomeGraph
import com.example.navigation.NavRoutes

@Composable
internal fun RootNavigationHost(
    navHostController: NavHostController,
    modifier: Modifier
) {
    NavHost(
        navController = navHostController,
        modifier = modifier,
        startDestination = NavRoutes.ExpenseList.route
    ) {
        expenseHomeGraph(navHostController)
    }
}