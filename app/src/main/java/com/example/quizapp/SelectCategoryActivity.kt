package com.example.quizapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat.applyTheme

class SelectCategoryActivity : AppCompatActivity() {

    private lateinit var categorySpinner: Spinner
    private lateinit var selectCategoryButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyTheme()
        setContentView(R.layout.activity_select_category)

        categorySpinner = findViewById(R.id.categorySpinner)
        selectCategoryButton = findViewById(R.id.selectCategoryButton)

        // Populate the Spinner with category options (you should fetch these categories from Firebase)
        val categories = arrayOf("Maths", "Science", "History") // Replace with your actual categories
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Handle category selection
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle no selection
            }
        }

        selectCategoryButton.setOnClickListener {
            val selectedCategory = categorySpinner.selectedItem.toString()
            // Pass the selected category to the next activity (e.g., ListQuestionsActivity)
            val intent = Intent(this, ListQuestionsActivity::class.java)
            intent.putExtra("SELECTED_CATEGORY", selectedCategory)
            startActivity(intent)
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
}
