package com.example.quizapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.database.*

class UpdateQuestionActivity : AppCompatActivity() {

    private lateinit var categorySpinner: Spinner
    private lateinit var questionEditText: EditText
    private lateinit var option1EditText: EditText
    private lateinit var option2EditText: EditText
    private lateinit var option3EditText: EditText
    private lateinit var answerEditText: EditText
    private lateinit var updateButton: Button

    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_question)

        categorySpinner = findViewById(R.id.categoryUpdateSpinner)
        questionEditText = findViewById(R.id.questionEditText)
        option1EditText = findViewById(R.id.option1EditText)
        option2EditText = findViewById(R.id.option2EditText)
        option3EditText = findViewById(R.id.option3EditText)
        answerEditText = findViewById(R.id.answerEditText)
        updateButton = findViewById(R.id.updateButton)

        val categories = arrayOf("Maths", "Science", "History") // Replace with your category names
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)

        // Set the adapter for the Spinner
        categorySpinner.adapter = adapter

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Handle the selected category
                val selectedCategory = categories[position]

                Toast.makeText(this@UpdateQuestionActivity, "Selected Category: $selectedCategory", Toast.LENGTH_SHORT).show()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case when nothing is selected
            }
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Quiz")

        // Get the selected question data from Intent
        val selectedQuestionText = intent.getStringExtra("SELECTED_QUESTION_TEXT") ?: ""
        val selectedOptions = intent.getStringArrayListExtra("SELECTED_OPTIONS") ?: emptyList<String>()
        val selectedAnswer = intent.getStringExtra("SELECTED_ANSWER") ?: ""
        val selectedCategory = intent.getStringExtra("SELECTED_CATEGORY") ?: ""

        // Populate the EditText fields with the extracted data
        questionEditText.setText(selectedQuestionText)
        option1EditText.setText(selectedOptions.getOrNull(0) ?: "")
        option2EditText.setText(selectedOptions.getOrNull(1) ?: "")
        option3EditText.setText(selectedOptions.getOrNull(2) ?: "")
        answerEditText.setText(selectedAnswer)

        // Set the selected category in the Spinner
        val categoryPosition = categories.indexOf(selectedCategory)
        if (categoryPosition != -1) {
            categorySpinner.setSelection(categoryPosition)
        }

        updateButton.setOnClickListener {
            updateQuestionInFirebase()
            finish()
        }
        val deleteButton = findViewById<Button>(R.id.deleteButton)
        deleteButton.setOnClickListener {
            deleteQuestionFromFirebase()
            finish()
        }
    }
    private fun updateQuestionInFirebase() {
        // Update the selected question in Firebase
        val updatedQuestionText = questionEditText.text.toString()
        val updatedOption1 = option1EditText.text.toString()
        val updatedOption2 = option2EditText.text.toString()
        val updatedOption3 = option3EditText.text.toString()
        val updatedAnswer = answerEditText.text.toString()
        val updatedCategory = categorySpinner.selectedItem.toString() // Get the selected category
        val selectedQuestionText = intent.getStringExtra("SELECTED_QUESTION_TEXT") ?: ""
        if (updatedQuestionText.isNotEmpty() && updatedOption1.isNotEmpty() && updatedOption2.isNotEmpty()
            && updatedOption3.isNotEmpty() && updatedAnswer.isNotEmpty() && updatedCategory.isNotEmpty()
        ) {
            val updatedQuizQuestion = Quiz(
                updatedQuestionText,
                updatedOption1,
                updatedOption2,
                updatedOption3,
                updatedAnswer,
                selectedQuestionText
            )

            // Remove the old question and add the updated question with the new category
            databaseReference.child(updatedQuestionText).removeValue()
            databaseReference.child(updatedQuestionText).setValue(updatedQuizQuestion)
            databaseReference.child(updatedQuestionText).child("category").setValue(updatedCategory)

            Toast.makeText(this, "Question updated successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteQuestionFromFirebase() {
        val updatedQuestionText = questionEditText.text.toString()
        val updatedCategory = categorySpinner.selectedItem.toString()

        if (updatedQuestionText.isNotEmpty() && updatedCategory.isNotEmpty()) {
            val questionRef = databaseReference.child(updatedQuestionText)

            // Remove the entire question node
            questionRef.removeValue()

            Toast.makeText(this, "Question deleted successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }




}
