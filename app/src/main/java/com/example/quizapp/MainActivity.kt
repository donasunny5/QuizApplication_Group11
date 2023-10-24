package com.example.quizapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {

    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var categoryList: ArrayList<CategoryItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val userButton = findViewById<Button>(R.id.userButton)
        val adminButton = findViewById<Button>(R.id.adminButton)

        userButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        adminButton.setOnClickListener {
            val intent = Intent(this, AddquizActivity::class.java)
            startActivity(intent)
        }
    }
}



