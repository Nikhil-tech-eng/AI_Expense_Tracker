package com.example.camerax

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import java.io.File

@Composable
fun ReceiptScannerScreen(
    onImageCaptured: (File) -> Unit
) {
    val context = LocalContext.current
    val cameraPermission = android.Manifest.permission.CAMERA
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                cameraPermission
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
    }

    LaunchedEffect(Unit) {
        if (!hasPermission) {
            launcher.launch(cameraPermission)
        }
    }
    if (hasPermission) {
        CameraPreview(onImageCaptured)
    } else {
        PermissionRationaleScreen(onRequest = {
            launcher.launch(android.Manifest.permission.CAMERA)
        })
    }
}