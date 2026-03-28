package com.vihaan.jarvisxline.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File

class ModelDownloader(private val context: Context) {
    private val client = OkHttpClient()

    suspend fun downloadSmolLm(token: String): File = withContext(Dispatchers.IO) {
        val modelDir = File(context.filesDir, "models/SmolLM2-135M-Instruct").apply { mkdirs() }
        val modelFile = File(modelDir, "model.safetensors")

        if (modelFile.exists()) return@withContext modelFile

        val request = Request.Builder()
            .url("https://huggingface.co/HuggingFaceTB/SmolLM2-135M-Instruct/resolve/main/model.safetensors")
            .header("Authorization", "Bearer $token")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                error("Failed to download model: HTTP ${response.code}")
            }
            val body = response.body ?: error("Empty body")
            modelFile.outputStream().use { output ->
                body.byteStream().copyTo(output)
            }
        }
        modelFile
    }
}
