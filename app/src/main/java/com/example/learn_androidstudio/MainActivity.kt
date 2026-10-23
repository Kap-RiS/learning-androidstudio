package com.example.learn_androidstudio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.learn_androidstudio.presentation.AppNavigation
import com.example.learn_androidstudio.presentation.theme.Learn_androidstudioTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Learn_androidstudioTheme {
                AppNavigation()
            }
        }
    }
}