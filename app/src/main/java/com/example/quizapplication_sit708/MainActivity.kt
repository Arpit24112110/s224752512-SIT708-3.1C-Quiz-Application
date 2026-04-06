package com.example.quizapplication_sit708

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var startButton: Button
    private lateinit var themeSwitch: Switch
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        sharedPreferences = getSharedPreferences("QuizAppPrefs", MODE_PRIVATE)

        // Apply saved theme before setting content view
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameInput = findViewById(R.id.nameInput)
        startButton = findViewById(R.id.startButton)
        themeSwitch = findViewById(R.id.themeSwitch)

        // Load saved name
        val savedName = sharedPreferences.getString("username", "")
        nameInput.setText(savedName)

        // Set switch state
        themeSwitch.isChecked = isDarkMode

        // Start button click
        startButton.setOnClickListener {
            val username = nameInput.text.toString().trim()

            if (username.isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            } else {
                sharedPreferences.edit().putString("username", username).apply()

                val intent = Intent(this, QuizActivity::class.java)
                intent.putExtra("username", username)
                startActivity(intent)
            }
        }

        // Theme switch logic
        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("dark_mode", isChecked).apply()

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            recreate()
        }
    }
}