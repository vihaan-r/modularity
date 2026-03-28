package com.vihaan.jarvisxline.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vihaan.jarvisxline.model.AppSettings
import com.vihaan.jarvisxline.data.XLineEngine

@Composable
fun SettingsScreen(
    settings: AppSettings,
    status: String,
    onBack: () -> Unit,
    onSaveToken: (String) -> Unit,
    onDownloadModel: () -> Unit,
    onSaveNavigatorPath: (String) -> Unit,
    onRegenerateApiKey: () -> Unit
) {
    var token by remember(settings.huggingFaceToken) { mutableStateOf(settings.huggingFaceToken) }
    var navigator by remember(settings.navigatorPath) { mutableStateOf(settings.navigatorPath) }

    Column(
        modifier = Modifier.fillMaxSize().padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineSmall)
        Text("Status: $status")
        Text("App API Key: ${settings.appApiKey}")
        Button(onClick = onRegenerateApiKey) { Text("Regenerate API Key") }

        OutlinedTextField(
            value = token,
            onValueChange = { token = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Hugging Face Access Token") }
        )
        Button(onClick = { onSaveToken(token) }) { Text("Save token") }
        Button(onClick = onDownloadModel) { Text("Download SmolLM2-135M-Instruct") }

        OutlinedTextField(
            value = navigator,
            onValueChange = { navigator = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Navigator Weights Path") }
        )
        Button(onClick = { onSaveNavigatorPath(navigator) }) { Text("Save navigator path") }

        Text("Fixed system prompt (cannot be changed):")
        Text(XLineEngine.FIXED_SYSTEM_PROMPT, style = MaterialTheme.typography.bodySmall)
        Button(onClick = onBack) { Text("Back to chat") }
    }
}
