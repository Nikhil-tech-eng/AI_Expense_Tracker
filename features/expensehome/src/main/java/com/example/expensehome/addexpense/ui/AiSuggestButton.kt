package com.example.expensehome.addexpense.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.design.theme.NeonBlue
import com.example.expensehome.R

@Composable
internal fun AiSuggestButton(
    onClick: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = NeonBlue,
            contentColor = Color.Black
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.height(48.dp)
    ) {
        Icon(Icons.Default.Star, contentDescription = "Suggest")
        Spacer(Modifier.width(8.dp))
        Text(text = stringResource(R.string.ask_ai_to_suggest_category))
    }
}

@Preview
@Composable
private fun PreviewAiButton() {
    AiSuggestButton(onClick = {}, isLoading = true)
}
