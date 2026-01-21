package com.example.mltoolkit

import android.graphics.BitmapFactory
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.tasks.await
import java.io.File

object ReceiptTextExtractor {
    suspend fun extractText(file: File): String {
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
        val image = InputImage.fromBitmap(bitmap,0)
        val recognizer = TextRecognition.getClient(
            TextRecognizerOptions.DEFAULT_OPTIONS
        )
        val result = recognizer.process(image).await()
        return result.text
    }
}