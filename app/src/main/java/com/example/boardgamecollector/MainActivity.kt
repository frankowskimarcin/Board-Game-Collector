package com.example.boardgamecollector

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

//    private val addButton: Button = findViewById(R.id.addBtn)
//    private val deleteButton: Button = findViewById(R.id.deleteBtn)
//    private val searchButton: Button = findViewById(R.id.searchBtn)
//    private val locationButton: Button = findViewById(R.id.locationBtn)
//    private val sortText: TextView = findViewById(R.id.sortTextView)
//    private val sortSpinner: Spinner = findViewById(R.id.sortSpinner)

//    private var listView: ListView? = null

    private var sortValue = "alfabetycznie"

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val addButton: Button = findViewById(R.id.addBtn)
        val searchButton: Button = findViewById(R.id.searchBtn)
        val locationButton: Button = findViewById(R.id.locationBtn)
        val sortText: TextView = findViewById(R.id.sortTextView)
        val sortSpinner: Spinner = findViewById(R.id.sortSpinner)
        val listView: ListView = findViewById(R.id.gamesList);
        loadAndDisplayGames()

        sortText.text = "Sortuj:"
        sortText.textSize = 18F
        sortText.gravity = Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL

        addItemsOnSpinner(sortSpinner)

        sortSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                sortValue = sortSpinner.selectedItem.toString()
                Log.i("TAG", sortValue)
                loadAndDisplayGames()
            }

        }

//        listView.setOnItemClickListener(object : AdapterView.OnItemClickListener {
//            fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                val intent = Intent(context, GameDetailsActivity::class.java)
//                val message = listView.selectedItem
//                intent.putExtra("game", message)
//                Log.i("TAG", message)
//                startActivity(intent)
//            }
//        })

        listView.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, GameDetailsActivity::class.java)

            val dbHandler = MyDBHandler(this, null, null, 1)
            val games = dbHandler.getAllGames(sortValue)
            val message = games[position].title

            intent.putExtra("game", message.toString())
            Log.i("TAG", message.toString())
            startActivity(intent)
        }
    }

    override fun onRestart() {
        super.onRestart()
        loadAndDisplayGames()
    }

    fun showActivityAddButton(v: View){
        val i = Intent(this, AddGameActivity::class.java)
        startActivity(i)
    }

    fun showActivitySearchButton(v: View){
        val i = Intent(this, ImportBggActivity::class.java)
        startActivity(i)
    }

    fun showActivityLocationButton(v: View){
        val i = Intent(this, LocationsActivity::class.java)
        startActivity(i)
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

    private fun loadAndDisplayGames() {
        val dbHandler = MyDBHandler(this, null, null, 1)
        val games = dbHandler.getAllGames(sortValue)
        val gameAdapter = GameAdapter(this, games)
        val listView: ListView = findViewById(R.id.gamesList);
        listView.adapter = gameAdapter


    }
}