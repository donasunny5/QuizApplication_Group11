package com.example.quizapp


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat.applyTheme
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.example.quizapp.QuestionAdapter

class ListQuestionsActivity : AppCompatActivity() {

    private lateinit var categoryLabel: TextView
    private lateinit var questionRecyclerView: RecyclerView
    private lateinit var selectedCategory: String
    private val questionsList: MutableList<QuizActivity.QuizQuestion> = mutableListOf()
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyTheme()
        setContentView(R.layout.activity_list_questions)

        categoryLabel = findViewById(R.id.categoryLabel)
        questionRecyclerView = findViewById(R.id.questionRecyclerView)

        // Retrieve the selected category passed from the previous activity
        selectedCategory = intent.getStringExtra("SELECTED_CATEGORY") ?: ""

        categoryLabel.text = "Selected Category: $selectedCategory"

        // Initialize and configure the RecyclerView
        questionRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = QuestionAdapter(questionsList) { selectedQuestion ->
            // Handle question selection for updating

            // Create an Intent to start the UpdateQuestionActivity
            val intent = Intent(this, UpdateQuestionActivity::class.java)

            // Pass the selected question details as extras to the intent

            intent.putExtra("SELECTED_QUESTION_TEXT", selectedQuestion.questionText)
            intent.putStringArrayListExtra("SELECTED_OPTIONS", ArrayList(selectedQuestion.options))
            intent.putExtra("SELECTED_ANSWER", selectedQuestion.correctOptionIndex)
            intent.putExtra("SELECTED_CATEGORY", selectedCategory)
            // Start the UpdateQuestionActivity
            startActivity(intent)
        }
        questionRecyclerView.adapter = adapter

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Quiz")

        // Fetch questions from Firebase for the selected category and populate questionsList
        fetchQuestionsFromFirebase(selectedCategory)
    }

    private fun fetchQuestionsFromFirebase(category: String) {
        // Retrieve questions for the selected category from Firebase
        databaseReference.orderByChild("category").equalTo(category).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                questionsList.clear()

                for (questionSnapshot in dataSnapshot.children) {
                    val quiz = questionSnapshot.getValue(Quiz::class.java)

                    // Ensure the fetched question is not null and has the correct category
                    if (quiz != null) {
                        val quizQuestion = QuizActivity.QuizQuestion(
                            quiz.question ?: "",
                            listOf(quiz.option1 ?: "", quiz.option2 ?: "", quiz.option3 ?: ""),
                            quiz.answer ?: ""
                        )
                        questionsList.add(quizQuestion)
                    }
                }

                // Notify the adapter that the data has changed
                questionRecyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
            }
        })
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
}
