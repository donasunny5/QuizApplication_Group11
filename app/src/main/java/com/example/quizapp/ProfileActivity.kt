package com.example.quizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class ProfileActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_activity)

        etName = findViewById(R.id.etName)
        btnSave = findViewById(R.id.btnSave)

        btnSave.setOnClickListener {
            val userName = etName.text.toString()

            // Check if the user entered a name
            if (userName.isNotBlank()) {
                val intent = Intent(this, CategoryList::class.java)
                intent.putExtra("USER_NAME", userName)
                etName.text.clear()
                startActivity(intent)
            } else {
                // Show an error message if the name is not entered
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            }
        }


    }
}
