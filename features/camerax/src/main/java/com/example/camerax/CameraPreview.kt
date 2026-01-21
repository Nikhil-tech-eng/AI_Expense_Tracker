package com.example.camerax

import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.io.File
import java.util.concurrent.Executors

@Composable
fun CameraPreview(onImageCaptured: (File) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }

    val imageCapture = remember {
        ImageCapture.Builder().build()
    }

    val cameraProvider = remember(cameraProviderFuture) {
        cameraProviderFuture.get()
    }

    DisposableEffect(lifecycleOwner) {
        onDispose {
            try {
                cameraProvider.unbindAll()
            } catch (e: Exception) {
                Log.e("ReceiptScannerScreen", "Error unbinding camera", e)
            }
        }
    }

    val executor = remember {
        Executors.newSingleThreadExecutor()
    }
    Column(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                val preview = Preview.Builder().build()
                preview.surfaceProvider = previewView.surfaceProvider
                try {
                    cameraProvider.unbindAll()
                    cameraProviderFuture.get().bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        imageCapture
                    )
                } catch (e: Exception) {
                    Log.e("ReceiptScannerScreen", "Error binding camera in android view", e)
                }
                previewView
            },
            modifier = Modifier.weight(1f)
        )

        Button(
            onClick = {
                val file = File(context.cacheDir, "receipt_${System.currentTimeMillis()}.jpg")
                val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
                imageCapture.takePicture(
                    outputOptions,
                    executor,
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onError(exception: ImageCaptureException) {
                            Log.e("ReceiptScannerScreen", "Image capture failed", exception)

                        }

                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            Log.d("ReceiptScannerScreen", "Image captured: ${file.absolutePath}")
                            onImageCaptured(file)
                        }
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(stringResource(R.string.capture_receipt))
        }
    }
}