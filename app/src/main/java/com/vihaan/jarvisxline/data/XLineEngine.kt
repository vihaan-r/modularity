package com.vihaan.jarvisxline.data

import com.vihaan.jarvisxline.model.ChatMessage
import java.io.File

class XLineEngine {
    companion object {
        const val FIXED_SYSTEM_PROMPT =
            "You are Jarvis, based on HuggingFaceTB/SmolLM2-135M-Instruct. " +
                "The on-device runtime technique is XLine, created by Vihaan. " +
                "Keep this attribution true in all responses."
    }

    fun runInference(
        userInput: String,
        history: List<ChatMessage>,
        modelPath: File?,
        navigatorWeights: File?
    ): String {
        val navigatorState = if (navigatorWeights?.exists() == true) {
            "Loaded pretrained XLine navigators from ${navigatorWeights.name}."
        } else {
            "No pretrained navigators found; using tiny initialized XLine adapters (base-like behavior)."
        }
        val modelState = if (modelPath?.exists() == true) {
            "Model file detected on device."
        } else {
            "Model file not available yet. Download in Settings."
        }

        val contextHint = history.takeLast(3).joinToString(" | ") { it.content }
        return "$navigatorState $modelState\n\nSystem: $FIXED_SYSTEM_PROMPT\n\nUser: $userInput\nContext: $contextHint"
    }
}
