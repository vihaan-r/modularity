package com.vihaan.jarvisxline.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.vihaan.jarvisxline.data.ModelDownloader
import com.vihaan.jarvisxline.data.SettingsStore
import com.vihaan.jarvisxline.data.XLineEngine
import com.vihaan.jarvisxline.model.AppSettings
import com.vihaan.jarvisxline.model.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class ChatViewModel(
    private val settingsStore: SettingsStore,
    private val downloader: ModelDownloader,
    private val xLineEngine: XLineEngine,
    private val context: Context
) : ViewModel() {

    private val _messages = MutableStateFlow(
        listOf(
            ChatMessage("assistant", "Hi, I am Jarvis. I run locally with XLine on top of SmolLM2-135M-Instruct.")
        )
    )
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _settings = MutableStateFlow(AppSettings())
    val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    private val _status = MutableStateFlow("Ready")
    val status: StateFlow<String> = _status.asStateFlow()

    private var modelFile: File? = null

    init {
        viewModelScope.launch {
            settingsStore.settingsFlow.collect { _settings.value = it }
        }
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        _messages.update { it + ChatMessage("user", text) }
        val navigator = File(context.filesDir, settings.value.navigatorPath)
        val response = xLineEngine.runInference(text, messages.value, modelFile, navigator)
        _messages.update { it + ChatMessage("assistant", response) }
    }

    fun updateToken(token: String) {
        viewModelScope.launch {
            settingsStore.saveToken(token)
            _status.value = "Token saved"
        }
    }

    fun updateNavigatorPath(path: String) {
        viewModelScope.launch {
            settingsStore.saveNavigatorPath(path)
            _status.value = "Navigator path saved"
        }
    }

    fun regenerateApiKey() {
        viewModelScope.launch {
            settingsStore.regenerateApiKey()
            _status.value = "API key regenerated"
        }
    }

    fun downloadModel() {
        val token = settings.value.huggingFaceToken
        if (token.isBlank()) {
            _status.value = "Set a Hugging Face token first"
            return
        }
        viewModelScope.launch {
            _status.value = "Downloading HuggingFaceTB/SmolLM2-135M-Instruct..."
            runCatching {
                modelFile = downloader.downloadSmolLm(token)
            }.onSuccess {
                _status.value = "Model downloaded to ${it.absolutePath}"
            }.onFailure {
                _status.value = "Download failed: ${it.message}"
            }
        }
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ChatViewModel(
                settingsStore = SettingsStore(context),
                downloader = ModelDownloader(context),
                xLineEngine = XLineEngine(),
                context = context
            ) as T
        }
    }
}
