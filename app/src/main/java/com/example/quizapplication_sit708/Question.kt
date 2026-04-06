package com.example.quizapplication_sit708

data class Question(
    val questionText: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)