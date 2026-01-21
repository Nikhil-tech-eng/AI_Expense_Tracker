package com.example.aiexpensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.aiexpensetracker.navigation.RootNavigationHost
import com.example.design.theme.AIExpenseTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            AIExpenseTrackerTheme(darkTheme = false) {
                RootNavigationHost(
                    navHostController = navController,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
