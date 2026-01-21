package com.example.expensehome.addexpense.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.design.theme.NeonBlue
import com.example.expensehome.R
import kotlinx.coroutines.delay

@Composable
internal fun AiPulseLoader() {
    var scale by remember { mutableStateOf(1f) }
    LaunchedEffect(Unit) {
        while (true) {
            scale = 1.1f
            delay(100)
            scale = 1.2f
            delay(100)
            scale = 1.3f
            delay(100)
            scale = 1.4f
            delay(100)
            scale = 1.5f
            delay(100)
            scale = 1.4f
            delay(100)
            scale = 1.3f
            delay(100)
            scale = 1.2f
            delay(100)
            scale = 1.1f
            delay(100)
            scale = 1f
            delay(100)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(400.dp)
                .graphicsLayer(scaleX = scale, scaleY = scale)
                .background(
                    Brush.radialGradient(
                        listOf(
                            NeonBlue,
                            Color.Transparent
                        )
                    ), shape = CircleShape
                )
        )
//        Text(
//            text = stringResource(R.string.extracting_with_ai),
//            style = MaterialTheme.typography.titleMedium,
//            color = Color.DarkGray
//        )
    }
}