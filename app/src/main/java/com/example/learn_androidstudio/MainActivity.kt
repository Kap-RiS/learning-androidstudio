package com.example.learn_androidstudio

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    private lateinit var etInputText: EditText
    private lateinit var btnOpenSecondActivity: Button
    private lateinit var btnCall: Button
    private lateinit var btnShare: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etInputText = findViewById(R.id.etInputText)
        btnOpenSecondActivity = findViewById(R.id.btnOpenSecondActivity)
        btnCall = findViewById(R.id.btnCall)
        btnShare = findViewById(R.id.btnShare)

        // Явный Intent
        btnOpenSecondActivity.setOnClickListener {
            val text = etInputText.text.toString().trim()
            if (validateInput(text)) {
                val intent = Intent(this, SecondActivity::class.java).apply {
                    putExtra("EXTRA_TEXT", text)
                }
                startActivity(intent)
            }
        }
        //Неявный Intent
        btnCall.setOnClickListener {
            val phone = etInputText.text.toString().trim()
            if (validatePhoneNumber(phone)) {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$phone")
                }
                startActivity(intent)
            }
        }
        //Системный Intent
        btnShare.setOnClickListener {
            val text = etInputText.text.toString().trim()
            if (validateInput(text)) {
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, text)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, "Поделиться через...")
                startActivity(shareIntent)
            }
        }
    }

    private fun validateInput(text: String): Boolean {
        return if (text.isEmpty()) {
            etInputText.error = "Поле не должно быть пустым!"
            Toast.makeText(this, "Заполните поле ввода", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }

    private fun validatePhoneNumber(phone: String): Boolean {
        return when {
            phone.isEmpty() -> {
                etInputText.error = "Введите номер телефона!"
                Toast.makeText(this, "Поле телефона пустое", Toast.LENGTH_SHORT).show()
                false
            }
            !Patterns.PHONE.matcher(phone).matches() || phone.length < 3 -> {
                etInputText.error = "Некорректный формат телефона!"
                Toast.makeText(this, "Введите валидный номер (например, +79991112233)", Toast.LENGTH_LONG).show()
                false
            }
            else -> true
        }
    }
}