package com.example.learn_androidstudio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.learn_androidstudio.ui.theme.Learn_androidstudioTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Learn_androidstudioTheme {
                AppListScreen(apps = mockAppList)
            }
        }
    }
}