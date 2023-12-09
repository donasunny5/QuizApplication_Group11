package com.example.quizapp

import android.content.Context
import android.os.Bundle
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class Settings : AppCompatActivity() {

        private lateinit var themeToggleButton: ToggleButton

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            applyTheme()
            setContentView(R.layout.settings)

            themeToggleButton = findViewById(R.id.themeToggleButton)

            // Check the current theme and set the toggle button state
            val isDarkMode = getSharedPreferences("Settings", Context.MODE_PRIVATE)
                .getBoolean("DarkMode", false)
            themeToggleButton.isChecked = isDarkMode

            themeToggleButton.setOnCheckedChangeListener { _, isChecked ->
                // Save the selected theme mode in SharedPreferences
                val editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
                editor.putBoolean("DarkMode", isChecked)
                editor.apply()

                // Recreate the activity to apply the new theme
                recreate()
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
