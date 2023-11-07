package com.example.quizapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class QuizActivity : AppCompatActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var answerRadioGroup: RadioGroup
    private lateinit var submitButton: Button
    private lateinit var backButton: Button

    private val quizData: MutableList<QuizQuestion> = mutableListOf()
    private var currentCategory: String = ""
    private var currentQuestionIndex = 0
    private var score = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz_activity)

        FirebaseApp.initializeApp(this);


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        questionTextView = findViewById(R.id.questionTextView)
        answerRadioGroup = findViewById(R.id.answerRadioGroup)
        submitButton = findViewById(R.id.submitButton)
        backButton = findViewById(R.id.backButton)
        currentCategory = intent.getStringExtra("CATEGORY") ?: ""

        loadQuizQuestions(currentCategory)

        submitButton.setOnClickListener {
            checkAnswer()
        }
    }

    data class QuizQuestion(
        val questionText: String,
        val options: List<String>,
        val correctOptionIndex: String
    ) {
        fun isCorrect(selectedOption: String): Boolean {
            // Convert correctOptionIndex to an integer for index access
            val correctIndex = correctOptionIndex

            // Check if selectedOption matches the correct option
            return correctIndex != null  && correctIndex == selectedOption
        }
    }

    private fun loadQuizQuestions(category: String) {
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Quiz") // Change "Quiz" to "questions"

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                quizData.clear()
                for (questionSnapshot in dataSnapshot.children) {
                    val quiz = questionSnapshot.getValue(Quiz::class.java)
                    if (quiz != null && quiz.category == category) {
                        val quizQuestion = QuizQuestion(
                            quiz.question ?: "",
                            listOf(quiz.option1 ?: "", quiz.option2 ?: "", quiz.option3 ?: ""),
                            quiz.answer ?: ""
                        )

                        quizData.add(quizQuestion)
                    }
                }
                if (quizData.isNotEmpty()) {
                    showQuestion()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
            }
        })
    }



    private fun showQuestion() {
        if (currentQuestionIndex < quizData.size) {
            val currentQuestion = quizData[currentQuestionIndex]

            questionTextView.text = currentQuestion.questionText

            answerRadioGroup.removeAllViews()
            for (i in currentQuestion.options.indices) {
                val radioButton = RadioButton(this)
                radioButton.text = currentQuestion.options[i]
                radioButton.id = View.generateViewId()
                answerRadioGroup.addView(radioButton)
                backButton.visibility = View.GONE
            }
        } else {
            questionTextView.text = "Quiz completed.Your score: $score"
            answerRadioGroup.removeAllViews()
            submitButton.visibility = View.GONE
            backButton.isVisible = true
            backButton.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun checkAnswer() {
        if (currentQuestionIndex < quizData.size) {
            val selectedRadioButtonId = answerRadioGroup.checkedRadioButtonId

            if (selectedRadioButtonId != -1) {
                val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
                val selectedAnswer = selectedRadioButton.text.toString()

                val currentQuestion = quizData[currentQuestionIndex]

                if (currentQuestion.isCorrect(selectedAnswer)) {
                    score++
                }

                currentQuestionIndex++
                showQuestion()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }
}
