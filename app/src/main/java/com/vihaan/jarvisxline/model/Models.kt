package com.vihaan.jarvisxline.model

data class ChatMessage(
    val role: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class AppSettings(
    val huggingFaceToken: String = "",
    val appApiKey: String = "",
    val navigatorPath: String = "navigator_weights.pt"
)
