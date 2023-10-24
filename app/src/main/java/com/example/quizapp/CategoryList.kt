package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CategoryList : AppCompatActivity() {

    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var categoryList: ArrayList<CategoryItem>
    //private lateinit var UserName:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.category_list)

        // Display userName in a TextView
        val userName = intent.getStringExtra("USER_NAME")

        val name = findViewById<TextView>(R.id.UserName)

        // Display the user's name in the TextView
        name.text = "Hello, $userName ,Welcome to Our Quiz Application"

        categoryRecyclerView = findViewById(R.id.categoryListRecyclerView)
        categoryRecyclerView.setHasFixedSize(true)
        categoryRecyclerView.layoutManager = LinearLayoutManager(this)

        categoryList = ArrayList()
        // Replace with your actual quiz categories
        categoryList.add(CategoryItem("Maths"))
        categoryList.add(CategoryItem("Science"))
        categoryList.add(CategoryItem("History"))
        // Add more categories as needed

        val adapter = CategoryAdapter(categoryList, object : CategoryAdapter.OnCategoryClickListener {
            override fun onCategoryClick(position: Int) {
                // Handle category item click here, e.g., start QuizActivity.kt
                val selectedCategory = categoryList[position].categoryName
                val intent = Intent(this@CategoryList, `QuizActivity`::class.java)
                intent.putExtra("CATEGORY", selectedCategory)
                startActivity(intent)
            }
        })
        categoryRecyclerView.adapter = adapter
    }
}
