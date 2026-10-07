package com.example.learn_androidstudio

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.learn_androidstudio.ui.theme.Learn_androidstudioTheme

class MainActivity : ComponentActivity() {

    private lateinit var etInputText: EditText
    private lateinit var btnOpenSecondActivity: Button
    private lateinit var btnCall: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etInputText = findViewById(R.id.etInputText)
        btnOpenSecondActivity = findViewById(R.id.btnOpenSecondActivity)
        btnCall = findViewById(R.id.btnCall)


        btnOpenSecondActivity.setOnClickListener {
            val text = etInputText.text.toString().trim()
            if (validateInput(text)){
                val intent = Intent(this, SecondActivity::class.java).apply {
                    putExtra("EXTRA_TEXT", text)
                }
                startActivity(intent)
            }
        }

        btnCall.setOnClickListener {
            val phone = etInputText.text.toString().trim()
            if (validatePhoneNumber(phone)) {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$phone")
                }
                startActivity(intent)
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
              Toast.makeText(
                  this,
                  "Введите валидный номер (например, +79991112233)",
                  Toast.LENGTH_LONG
              ).show()
              false
          }
            else -> true
          }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Learn_androidstudioTheme {
        Greeting("Android")
    }
}