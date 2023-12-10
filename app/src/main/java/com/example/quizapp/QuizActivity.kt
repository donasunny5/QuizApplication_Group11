package com.example.quizapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat.applyTheme
import androidx.core.view.isVisible
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.TimeUnit

class QuizActivity : AppCompatActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var answerRadioGroup: RadioGroup
    private lateinit var submitButton: Button
    private lateinit var backButton: Button

    private lateinit var timerTextView: TextView
    private lateinit var countDownTimer: CountDownTimer
    private var timeLeftInMillis: Long = 10000 // 60 seconds
    private val countdownInterval: Long = 1000 // 1 second

    private val quizData: MutableList<QuizQuestion> = mutableListOf()
    private var currentCategory: String = ""
    private var currentQuestionIndex = 0
    private var score = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyTheme()
        setContentView(R.layout.quiz_activity)

        FirebaseApp.initializeApp(this);

        timerTextView = findViewById(R.id.timerTextView)


        startTimer()

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
countDownTimer.start()
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

            countDownTimer.cancel()
            val totalQuestions = quizData.size
            val percentage = (score.toDouble() / totalQuestions) * 100

            if (percentage >= 100) {
                questionTextView.text = "Quiz completed.\nYour Score: $score/$totalQuestions\nExcellent! You got $percentage%. You got all questions correct."

            } else if (percentage >= 75) {
                questionTextView.text = "Quiz completed.\nYour Score: $score/$totalQuestions\nGood job! You performed well with $percentage%."

            } else {
                questionTextView.text = "Quiz completed.\nYour Score: $score/$totalQuestions\nNeed to improve. You scored $percentage%."
            }

            answerRadioGroup.removeAllViews()
            submitButton.visibility = View.GONE
            backButton.isVisible = true
            backButton.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

            // Print the result and score to the console
            println("Result: ${questionTextView.text}")
            println("Score: $score")
            countDownTimer.cancel()
            timerTextView.visibility = View.GONE
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
    private fun applyTheme() {
        val isDarkMode = getSharedPreferences("Settings", Context.MODE_PRIVATE)
            .getBoolean("DarkMode", false)

        if (isDarkMode) {
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.AppTheme)
        }
    }
    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timeLeftInMillis, countdownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimerText()

            }

            override fun onFinish() {

                currentQuestionIndex++
                showQuestion()

            }
        }

        countDownTimer.start()
    }
    private fun updateTimerText() {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeLeftInMillis)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeLeftInMillis) % 60
        val timeString = String.format("%02d:%02d", minutes, seconds)
        timerTextView.text = timeString

    }

    // Make sure to cancel the timer when the activity is destroyed

}

