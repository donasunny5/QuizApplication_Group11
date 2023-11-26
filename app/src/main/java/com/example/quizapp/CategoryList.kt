package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import android.view.Menu
import android.widget.SearchView

import java.util.*
import kotlin.collections.ArrayList

class CategoryList : AppCompatActivity() {

    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var categoryList: ArrayList<CategoryItem>
    private lateinit var searchView: SearchView

    private lateinit var adapter: CategoryAdapter
    private lateinit var originalCategoryList: ArrayList<CategoryItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.category_list)

        // Initialize views
        categoryRecyclerView = findViewById(R.id.categoryListRecyclerView)
        searchView = findViewById(R.id.searchView)

        // Display userName in a TextView
        val userName = intent.getStringExtra("USER_NAME")
        val name = findViewById<TextView>(R.id.UserName)
        name.text = "Hello, $userName, Welcome to Our Quiz Application"

        // Replace with your actual quiz categories
        categoryList = ArrayList()
        categoryList.add(CategoryItem("Maths"))
        categoryList.add(CategoryItem("Science"))
        categoryList.add(CategoryItem("History"))

        // Set up the original category list
        originalCategoryList = ArrayList(categoryList)

        // Set up the RecyclerView and adapter
        categoryRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CategoryAdapter(categoryList, object : CategoryAdapter.OnCategoryClickListener {
            override fun onCategoryClick(position: Int) {
                val selectedCategory = categoryList[position].categoryName
                val intent = Intent(this@CategoryList, QuizActivity::class.java)
                intent.putExtra("CATEGORY", selectedCategory)
                startActivity(intent)
            }
        })
        categoryRecyclerView.adapter = adapter

        // Set up the search functionality
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterCategoryList(newText)
                return true
            }
        })
    }

    private fun filterCategoryList(query: String?) {
        val filteredList = ArrayList<CategoryItem>()

        for (item in originalCategoryList) {
            if (item.categoryName.toLowerCase(Locale.ROOT).contains(query?.toLowerCase(Locale.ROOT) ?: "")) {
                filteredList.add(item)
            }
        }

        categoryList.clear()
        categoryList.addAll(filteredList)
        adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
}
