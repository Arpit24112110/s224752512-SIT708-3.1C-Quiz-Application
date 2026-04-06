package com.example.quizapplication_sit708

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class QuizActivity : AppCompatActivity() {

    private lateinit var questionText: TextView
    private lateinit var progressText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var option1: Button
    private lateinit var option2: Button
    private lateinit var option3: Button
    private lateinit var option4: Button
    private lateinit var submitButton: Button
    private lateinit var nextButton: Button
    private lateinit var themeSwitchQuiz: Switch
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var questions: List<Question>
    private var currentQuestionIndex = 0
    private var selectedAnswerIndex = -1
    private var score = 0
    private var isSubmitted = false
    private var username = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences = getSharedPreferences("QuizAppPrefs", MODE_PRIVATE)

        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        username = intent.getStringExtra("username") ?: ""

        questionText = findViewById(R.id.questionText)
        progressText = findViewById(R.id.progressText)
        progressBar = findViewById(R.id.progressBar)
        option1 = findViewById(R.id.option1)
        option2 = findViewById(R.id.option2)
        option3 = findViewById(R.id.option3)
        option4 = findViewById(R.id.option4)
        submitButton = findViewById(R.id.submitButton)
        nextButton = findViewById(R.id.nextButton)
        themeSwitchQuiz = findViewById(R.id.themeSwitchQuiz)

        themeSwitchQuiz.isChecked = isDarkMode

        questions = listOf(
            Question(
                "What is the capital of France?",
                listOf("Berlin", "Madrid", "Paris", "Rome"),
                2
            ),
            Question(
                "Which planet is known as the Red Planet?",
                listOf("Earth", "Mars", "Jupiter", "Venus"),
                1
            ),
            Question(
                "What is 5 + 7?",
                listOf("10", "11", "12", "13"),
                2
            ),
            Question(
                "Which is the largest ocean on Earth?",
                listOf("Atlantic", "Indian", "Pacific", "Arctic"),
                2
            ),
            Question(
                "Which language is primarily used for Android development?",
                listOf("Python", "Java", "Swift", "Ruby"),
                1
            )
        )

        loadQuestion()

        option1.setOnClickListener { selectAnswer(0) }
        option2.setOnClickListener { selectAnswer(1) }
        option3.setOnClickListener { selectAnswer(2) }
        option4.setOnClickListener { selectAnswer(3) }

        submitButton.setOnClickListener {
            if (selectedAnswerIndex == -1) {
                Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show()
            } else if (!isSubmitted) {
                submitAnswer()
            }
        }

        nextButton.setOnClickListener {
            if (!isSubmitted) {
                Toast.makeText(this, "Please submit your answer first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (currentQuestionIndex < questions.size - 1) {
                currentQuestionIndex++
                loadQuestion()
            } else {
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra("username", username)
                intent.putExtra("score", score)
                intent.putExtra("totalQuestions", questions.size)
                startActivity(intent)
                finish()
            }
        }

        themeSwitchQuiz.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("dark_mode", isChecked).apply()

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            recreate()
        }
    }

    private fun loadQuestion() {
        val currentQuestion = questions[currentQuestionIndex]

        questionText.text = currentQuestion.questionText
        option1.text = currentQuestion.options[0]
        option2.text = currentQuestion.options[1]
        option3.text = currentQuestion.options[2]
        option4.text = currentQuestion.options[3]

        progressText.text = "Question ${currentQuestionIndex + 1}/${questions.size}"
        progressBar.progress = ((currentQuestionIndex + 1) * 100) / questions.size

        selectedAnswerIndex = -1
        isSubmitted = false

        resetButtonColors()
        enableOptionButtons(true)
    }

    private fun selectAnswer(index: Int) {
        if (isSubmitted) return

        selectedAnswerIndex = index
        resetButtonColors()

        when (index) {
            0 -> option1.setBackgroundColor(Color.LTGRAY)
            1 -> option2.setBackgroundColor(Color.LTGRAY)
            2 -> option3.setBackgroundColor(Color.LTGRAY)
            3 -> option4.setBackgroundColor(Color.LTGRAY)
        }
    }

    private fun submitAnswer() {
        isSubmitted = true
        val correctIndex = questions[currentQuestionIndex].correctAnswerIndex

        if (selectedAnswerIndex == correctIndex) {
            score++
        }

        when (correctIndex) {
            0 -> option1.setBackgroundColor(Color.GREEN)
            1 -> option2.setBackgroundColor(Color.GREEN)
            2 -> option3.setBackgroundColor(Color.GREEN)
            3 -> option4.setBackgroundColor(Color.GREEN)
        }

        if (selectedAnswerIndex != correctIndex) {
            when (selectedAnswerIndex) {
                0 -> option1.setBackgroundColor(Color.RED)
                1 -> option2.setBackgroundColor(Color.RED)
                2 -> option3.setBackgroundColor(Color.RED)
                3 -> option4.setBackgroundColor(Color.RED)
            }

            when (correctIndex) {
                0 -> option1.setBackgroundColor(Color.GREEN)
                1 -> option2.setBackgroundColor(Color.GREEN)
                2 -> option3.setBackgroundColor(Color.GREEN)
                3 -> option4.setBackgroundColor(Color.GREEN)
            }
        }

        enableOptionButtons(false)
    }

    private fun enableOptionButtons(enable: Boolean) {
        option1.isEnabled = enable
        option2.isEnabled = enable
        option3.isEnabled = enable
        option4.isEnabled = enable
    }

    private fun resetButtonColors() {
        val defaultColor = getColor(com.google.android.material.R.color.design_default_color_primary)
        option1.setBackgroundColor(defaultColor)
        option2.setBackgroundColor(defaultColor)
        option3.setBackgroundColor(defaultColor)
        option4.setBackgroundColor(defaultColor)
    }
}