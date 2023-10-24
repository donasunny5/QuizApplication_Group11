package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.FirebaseApp


class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        registerButton = findViewById(R.id.registerButton)


        try {
            auth = FirebaseAuth.getInstance()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Initialize Firebase Authentication
        //auth = FirebaseAuth.getInstance()


        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Sign in with Firebase Authentication
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Login successful, navigate to CategoryListActivity
                        val intent = Intent(this, ProfileActivity::class.java)
                        startActivity(intent)
                        finish() // Close the login activity
                    } else {
                        // Login failed, handle the error
                        Toast.makeText(this, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        registerButton.setOnClickListener {
             val email = emailEditText.text.toString()
             val password = passwordEditText.text.toString()

             // Create a new user with Firebase Authentication
             auth.createUserWithEmailAndPassword(email, password)
                 .addOnCompleteListener(this) { task ->
                     if (task.isSuccessful) {
                         // Registration successful, you can log in the user automatically
                         // or display a message to confirm registration
                         Toast.makeText(this, "Registration successful.", Toast.LENGTH_SHORT).show()
                     } else {
                         // Registration failed, display an error message
                         Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show()
                     }
                 }


         }

    }
}
