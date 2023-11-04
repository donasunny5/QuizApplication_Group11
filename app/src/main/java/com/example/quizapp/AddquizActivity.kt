package com.example.quizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.quizapp.databinding.ActivityAddquizBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddquizActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddquizBinding
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddquizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val updateButton = binding.updateButton

        // Set a click listener for the update button
        updateButton.setOnClickListener {
            // Create an intent to navigate to the SelectCategoryActivity
            val intent = Intent(this, SelectCategoryActivity::class.java)
            startActivity(intent)
        }
        binding.saveBtn.setOnClickListener {

            val question = binding.question.text.toString()
            val option1 = binding.option1.text.toString()
            val option2 = binding.option2.text.toString()
            val option3 = binding.option3.text.toString()
            val answer = binding.answer.text.toString()
            val categorySpinner = binding.categorySpinner
            val category = categorySpinner.selectedItem.toString()
            database = FirebaseDatabase.getInstance().getReference("Quiz")
            val quiz = Quiz(question, option1, option2, option3, answer, category)
            database.child(question).setValue(quiz).addOnSuccessListener {

                binding.question.text.clear()
                binding.option1.text.clear()
                binding.option2.text.clear()
                binding.option3.text.clear()
                binding.answer.text.clear()
                categorySpinner.setSelection(0)


                Toast.makeText(this,"Successfully Saved",Toast.LENGTH_SHORT).show()

            }.addOnFailureListener{ error ->

                val errorMessage = error.message
                Toast.makeText(this, "Failed: $errorMessage", Toast.LENGTH_SHORT).show()


            }
        }
        binding.backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }
}