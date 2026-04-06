package com.example.quizapplication_sit708

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class ResultActivity : AppCompatActivity() {

    private lateinit var congratsText: TextView
    private lateinit var scoreText: TextView
    private lateinit var newQuizButton: Button
    private lateinit var finishButton: Button
    private lateinit var themeSwitchResult: Switch
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences = getSharedPreferences("QuizAppPrefs", MODE_PRIVATE)

        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        congratsText = findViewById(R.id.congratsText)
        scoreText = findViewById(R.id.scoreText)
        newQuizButton = findViewById(R.id.newQuizButton)
        finishButton = findViewById(R.id.finishButton)
        themeSwitchResult = findViewById(R.id.themeSwitchResult)

        val username = intent.getStringExtra("username") ?: "User"
        val score = intent.getIntExtra("score", 0)
        val totalQuestions = intent.getIntExtra("totalQuestions", 5)

        congratsText.text = "Congratulations $username!"
        scoreText.text = "Your Score: $score/$totalQuestions"

        themeSwitchResult.isChecked = isDarkMode

        newQuizButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        finishButton.setOnClickListener {
            finishAffinity()
        }

        themeSwitchResult.setOnCheckedChangeListener { _, isChecked ->
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