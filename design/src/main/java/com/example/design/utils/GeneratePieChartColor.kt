package com.example.design.utils

import androidx.compose.ui.graphics.Color
import kotlin.math.abs

fun generateColorForCategory(category: String): Color {
    val normalized = category.trim().lowercase()
    return when {
        normalized.contains("food") || normalized.contains("drink") || normalized.contains("restaurant") -> Color(0xFFEF5350) // Vibrant Red
        normalized.contains("grocery") -> Color(0xFF66BB6A) // Vibrant Green
        normalized.contains("travel") || normalized.contains("fuel") || normalized.contains("cab") -> Color(0xFF42A5F5) // Vibrant Blue
        normalized.contains("shopping") -> Color(0xFFAB47BC) // Purple
        normalized.contains("entertainment") || normalized.contains("movie") || normalized.contains("netflix") -> Color(0xFF26A69A) // Teal
        normalized.contains("bill") || normalized.contains("recharge") -> Color(0xFFFF7043) // Orange
        normalized == "other" -> Color(0xFF78909C) // Slate Grey
        else -> {
            // Generate stable dynamic color based on string hash code
            val colors = listOf(
                Color(0xFFEC407A), // Pink
                Color(0xFF7E57C2), // Deep Purple
                Color(0xFF26C6DA), // Cyan
                Color(0xFF9CCC65), // Light Green
                Color(0xFFFFCA28), // Amber
                Color(0xFFFFA726), // Light Orange
                Color(0xFF8D6E63), // Brown
                Color(0xFF5C6BC0)  // Indigo
            )
            val index = abs(category.hashCode()) % colors.size
            colors[index]
        }
    }
}
