package com.example.learn_androidstudio

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.ComponentActivity


class SecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_second)

        val tvRecievedText = findViewById<TextView>(R.id.tvReceivedText)
        val receivedText = intent.getStringExtra("EXTRA_TEXT")
        tvRecievedText.text = receivedText ?: "Данные не получены"
    }
}