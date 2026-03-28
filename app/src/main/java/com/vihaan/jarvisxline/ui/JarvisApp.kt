package com.vihaan.jarvisxline.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vihaan.jarvisxline.ui.screens.ChatScreen
import com.vihaan.jarvisxline.ui.screens.SettingsScreen
import com.vihaan.jarvisxline.viewmodel.ChatViewModel

@Composable
fun JarvisApp(context: Context) {
    val navController = rememberNavController()
    val factory = remember { ChatViewModel.Factory(context) }
    val vm: ChatViewModel = viewModel(factory = factory)

    val messages by vm.messages.collectAsState()
    val settings by vm.settings.collectAsState()
    val status by vm.status.collectAsState()

    NavHost(navController = navController, startDestination = "chat") {
        composable("chat") {
            ChatScreen(
                messages = messages,
                status = status,
                onSend = vm::sendMessage,
                onOpenSettings = { navController.navigate("settings") }
            )
        }
        composable("settings") {
            SettingsScreen(
                settings = settings,
                status = status,
                onBack = { navController.popBackStack() },
                onSaveToken = vm::updateToken,
                onDownloadModel = vm::downloadModel,
                onSaveNavigatorPath = vm::updateNavigatorPath,
                onRegenerateApiKey = vm::regenerateApiKey
            )
        }
    }
}
