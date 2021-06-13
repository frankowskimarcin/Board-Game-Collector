package com.example.boardgamecollector

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadGames()

        val addButton: Button = findViewById(R.id.addBtn)
        val deleteButton: Button = findViewById(R.id.deleteBtn)
        val searchButton: Button = findViewById(R.id.searchBtn)
        val locationButton: Button = findViewById(R.id.locationBtn)
        val sortText: TextView = findViewById(R.id.sortTextView)
        val sortSpinner: Spinner = findViewById(R.id.sortSpinner)

        sortText.text = "Sortuj:"
        sortText.textSize = 18F
        sortText.gravity = Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL

        addItemsOnSpinner(sortSpinner)

        setClickListeners(addButton, deleteButton, searchButton, locationButton)



    }

    private fun setClickListeners(addButton: Button, deleteButton: Button, searchButton: Button, locationButton: Button) {
        addButton.setOnClickListener{

        }

        deleteButton.setOnClickListener {

        }

        searchButton.setOnClickListener {

        }

        locationButton.setOnClickListener {

        }
    }

    private fun addItemsOnSpinner(sortSpinner: Spinner) {
        val list: MutableList<String> = ArrayList()
        list.add("alfabetycznie")
        list.add("data wydania")
        list.add("pozycja rankingu")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sortSpinner.adapter = adapter
    }

    private fun loadGames(){

    }
}