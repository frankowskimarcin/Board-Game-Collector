package com.example.boardgamecollector

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class LocationsActivity : AppCompatActivity() {

    var locationName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_locations)
        displayLocations()
    }

    fun onAddLocation(v: View){
        val dbHandler = MyDBHandler(this, null, null, 1)
        val location: EditText = findViewById(R.id.editTextNewLocation)
        val addButton: Button = findViewById(R.id.addLocBtn)
        val newLocation = location.text.toString()
        if (dbHandler.isLocationIdDb(newLocation)){
            Toast.makeText(this, "Lokalizacja juz istnieje", Toast.LENGTH_LONG).show()
        }
        else{
            dbHandler.addLocation(newLocation)
            location.text.clear()
            displayLocations()
        }
    }

    fun onEditLocationName(v: View){
        val dbHandler = MyDBHandler(this, null, null, 1)
        val locationText: EditText = findViewById(R.id.editTextLocName)
        if (locationText.text.toString() != locationName){
            if (dbHandler.isLocationIdDb(locationText.text.toString())){
                Toast.makeText(this, "Lokalizacja juz istnieje", Toast.LENGTH_LONG).show()
            }
            else{
                dbHandler.editLocation(locationName.toString(), locationText.text.toString())
                locationName = locationText.text.toString()
                displayLocations()
            }
        }
    }

    fun onDeleteLocation(v: View){
        val dbHandler = MyDBHandler(this, null, null, 1)

        if (!locationName.isNullOrEmpty() and dbHandler.isLocationEmpty(locationName.toString())){
            dbHandler.deleteLocation(locationName.toString())
        }
        else{
            Toast.makeText(this, "Lokalizacja nie moze zostac usunieta", Toast.LENGTH_LONG).show()
        }
        displayLocations()
    }

    @SuppressLint("SetTextI18n")
    private fun displayLocations(){
        val dbHandler = MyDBHandler(this, null, null, 1)
        val allLocScrollView: ScrollView = findViewById(R.id.allLocScrollView)
        val layout: RelativeLayout = findViewById(R.id.locDetailsLayout)
        allLocScrollView.visibility = View.VISIBLE
        layout.visibility = View.GONE

        val tableLayout: TableLayout = findViewById(R.id.allLocTableLayout)
        tableLayout.removeAllViews()
        val locations = dbHandler.getLocations()

        for (i in 0 until locations.size){
            val tableRow = TableRow(this)
            val locationTextView = TextView(this)
            locationTextView.textSize = 18F
            locationTextView.text = locations[i]
            tableRow.addView(locationTextView)
            tableLayout.addView(tableRow)

            tableRow.setOnClickListener {
                locationName = locations[i]
                val locEditText: EditText = findViewById(R.id.editTextLocName)
                locEditText.setText(locationName)
                val games = dbHandler.getGamesByLocation(locations[i])
                val gamesLayout: TableLayout = findViewById(R.id.gamesInLocTableLayout)

                if (games.isEmpty()){
                    val gameRow = TableRow(this)
                    val gameTextView = TextView(this)
                    gameTextView.textSize = 18F
                    gameTextView.text = "Brak gier"
                    gameRow.addView(gameTextView)
                    gamesLayout.addView(gameRow)
                }
                else{
                    for (j in 0 until games.size){
                        val gameRow = TableRow(this)
                        val gameTextView = TextView(this)
                        gameTextView.textSize = 18F
                        gameTextView.text = games[j]
                        gameRow.addView(gameTextView)
                        gamesLayout.addView(gameRow)

                    }
                }

                allLocScrollView.visibility = View.GONE
                layout.visibility = View.VISIBLE
            }
        }


    }
}