package com.vihaan.jarvisxline

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.vihaan.jarvisxline.ui.JarvisApp
import com.vihaan.jarvisxline.ui.theme.JarvisTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JarvisTheme {
                JarvisApp(applicationContext)
            }
        }
    }
}
