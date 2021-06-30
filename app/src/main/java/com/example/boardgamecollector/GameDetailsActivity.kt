package com.example.boardgamecollector

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.list_game.*
import java.time.LocalDate

class GameDetailsActivity : AppCompatActivity() {


    private var game = BoardGame()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_details)
        val scrollViewGameDetails: ScrollView = findViewById(R.id.ScrollViewDetails)
        val scrollViewEditDetails: ScrollView = findViewById(R.id.EditDetailsScrollView)

        scrollViewGameDetails.visibility = View.VISIBLE
        scrollViewEditDetails.visibility = View.GONE
        val title: String? = intent.getStringExtra("game")
        displayGameDetails(title.toString())

    }

    override fun onRestart() {
        super.onRestart()
        val scrollViewGameDetails: ScrollView = findViewById(R.id.ScrollViewDetails)
        val scrollViewEditDetails: ScrollView = findViewById(R.id.EditDetailsScrollView)
        scrollViewGameDetails.visibility = View.VISIBLE
        scrollViewEditDetails.visibility = View.GONE
        displayGameDetails(title.toString())
    }

    fun onEditGameButton(v: View){
        val scrollViewGameDetails: ScrollView = findViewById(R.id.ScrollViewDetails)
        val scrollViewEditDetails: ScrollView = findViewById(R.id.EditDetailsScrollView)
        scrollViewGameDetails.visibility = View.GONE
        scrollViewEditDetails.visibility = View.VISIBLE

        val dbHandler = MyDBHandler(this, null, null, 1)

        val yearNumberPicker: NumberPicker = findViewById(R.id.NumberPickerPublicationYear)
        val orderDateDatePicker: DatePicker = findViewById(R.id.DatePickerOrderDate)
        val addedDateDatePicker: DatePicker = findViewById(R.id.DatePickerAddedDate)
        val typeSpinner: Spinner = findViewById(R.id.spinnerType)
        val locationSpinner: Spinner = findViewById(R.id.spinnerLocation)

        yearNumberPicker.value = game.publicationYear!!
        yearNumberPicker.minValue = 1900
        yearNumberPicker.maxValue = 2100
        yearNumberPicker.wrapSelectorWheel = false
        var newPublicationYear = game.publicationYear
        yearNumberPicker.setOnValueChangedListener { _, _, value ->
            newPublicationYear = value
        }
        if(!game.orderDate.isNullOrEmpty())
            orderDateDatePicker.init(game.orderDate!!.substring(0,4).toInt(),
                game.orderDate!!.substring(5,7).toInt() - 1,
                game.orderDate!!.substring(8,10).toInt(),null)

        var newOrderDate = game.orderDate
        orderDateDatePicker.setOnDateChangedListener { datePicker, year, month, day ->
            newOrderDate = LocalDate.of(year, month + 1, day).toString()
        }

        if(!game.addedDate.isNullOrEmpty())
            addedDateDatePicker.init(game.addedDate!!.substring(0,4).toInt(),
                game.addedDate!!.substring(5,7).toInt() - 1,
                game.addedDate!!.substring(8,10).toInt(),null)

        var newAddedDate = game.addedDate
        addedDateDatePicker.setOnDateChangedListener { datePicker, year, month, day ->
            newAddedDate = LocalDate.of(year, month + 1, day).toString()
        }

        val typeList: MutableList<String> = ArrayList()
        typeList.add("podstawowa")
        typeList.add("dodatek")
        typeList.add("mieszana")
        val typeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, typeList)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        typeSpinner.adapter = typeAdapter
        if(!game.gameType.isNullOrEmpty())
            typeSpinner.setSelection(typeList.indexOf(game.gameType!!))
        var newGameType: String? = null
        typeSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                newGameType = typeSpinner.selectedItem.toString()
            }
        }

        val locationList = dbHandler.getLocations()
        val locationAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, locationList)
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        locationSpinner.adapter = locationAdapter
        if(!game.location.isNullOrEmpty())
            locationSpinner.setSelection(locationList.indexOf(game.location!!))
        var newLocation: String? = null
        locationSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                newLocation = locationSpinner.selectedItem.toString()
            }
        }

        val editTextOriginalTitle: EditText = findViewById(R.id.editTextOriginalTitle)
        val editTextDesigners: EditText = findViewById(R.id.editTextDesigners)
        val editTextArtists: EditText = findViewById(R.id.editTextArtists)
        val editTextDescription: EditText = findViewById(R.id.editTextDescription)
        val editTextPrice: EditText = findViewById(R.id.editTextPrice)
        val editTextSCD: EditText = findViewById(R.id.editTextSCD)
        val editTextCode: EditText = findViewById(R.id.editTextCode)
        val editTextProductionCod: EditText = findViewById(R.id.editTextProductionCod)
        val editTextExpansions: EditText = findViewById(R.id.editTextExpansions)
        val editTextComment: EditText = findViewById(R.id.editTextComment)

        editTextOriginalTitle.setText(game.originalTitle)
        editTextDesigners.setText(dbHandler.getDesigners(game.title.toString()).joinToString("\n"))
        editTextArtists.setText(dbHandler.getArtists(game.title.toString()).joinToString("\n"))
        editTextDescription.setText(game.description)
        editTextPrice.setText(game.price)
        editTextSCD.setText(game.scd)
        editTextCode.setText(game.code)
        editTextProductionCod.setText(game.productionCode)
        editTextExpansions.setText(dbHandler.getExpansions(game.title.toString()).joinToString("\n"))
        editTextComment.setText(game.comment)

        val saveEditDetailsButton: Button = findViewById(R.id.saveEditDetailsBtn)
        saveEditDetailsButton.setOnClickListener {
            game.publicationYear = newPublicationYear
            game.addedDate = newAddedDate
            game.orderDate = newOrderDate
            game.gameType = newGameType
            game.location = newLocation

            game.originalTitle = editTextOriginalTitle.text.toString()
            game.description = editTextDescription.text.toString()
            game.price = editTextPrice.text.toString()
            game.scd = editTextSCD.text.toString()
            game.code = editTextCode.text.toString()
            game.productionCode = editTextProductionCod.text.toString()
            game.comment = editTextComment.text.toString()

            dbHandler.updateDesigners(editTextDesigners.text.toString(), game.title.toString())
            dbHandler.updateArtists(editTextArtists.text.toString(), game.title.toString())
            dbHandler.updateExpansions(editTextExpansions.text.toString(), game.title.toString())
            dbHandler.updateGame(game)

            scrollViewGameDetails.visibility = View.VISIBLE
            scrollViewEditDetails.visibility = View.GONE
            displayGameDetails(game.title.toString())
        }

    }

    fun onDeleteGameButton(v: View){
        val dbHandler = MyDBHandler(this, null, null, 1)
        if (dbHandler.deleteGame(game.title.toString())){
            Toast.makeText(this, "Gra zostala usunieta", Toast.LENGTH_LONG).show()
        }

        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

//    fun onSaveEditDetailsButton(v: View){
//
//    }

    private fun displayGameDetails(title: String){
        val dbHandler = MyDBHandler(this, null, null, 1)
        val titleTextView: TextView = findViewById(R.id.textViewDetailsTitle)
        titleTextView.text = title
        game = dbHandler.findGame(title)

        val tvOriginalTitle: TextView = findViewById(R.id.tvOriginalTitle)
        val tvPublicationYear: TextView = findViewById(R.id.tvPublicationYear)
        val tvDesigners: TextView = findViewById(R.id.tvDesigners)
        val tvArtists: TextView = findViewById(R.id.tvArtists)
        val tvDescription: TextView = findViewById(R.id.tvDescription)
        val tvOrderDate: TextView = findViewById(R.id.tvOrderDate)
        val tvAddedDate: TextView = findViewById(R.id.tvAddedDate)
        val tvPrice: TextView = findViewById(R.id.tvPrice)
        val tvSCD: TextView = findViewById(R.id.tvSCD)
        val tvCode: TextView = findViewById(R.id.tvCode)
        val tvProductionCode: TextView = findViewById(R.id.tvProductionCode)
        val tvType: TextView = findViewById(R.id.tvType)
        val tvExpansions: TextView = findViewById(R.id.tvExpansions)
        val tvLocation: TextView = findViewById(R.id.tvLocation)
        val tvComment: TextView = findViewById(R.id.tvComment)

        tvOriginalTitle.text = game.originalTitle
        tvPublicationYear.text = game.publicationYear.toString()
        tvDesigners.text = dbHandler.getDesigners(title).joinToString("\n")
        tvArtists.text = dbHandler.getArtists(title).joinToString("\n")
        tvDescription.text = game.description
        tvOrderDate.text = game.orderDate
        tvAddedDate.text = game.addedDate
        tvPrice.text = game.price
        tvSCD.text = game.scd
        tvCode.text = game.code
        tvProductionCode.text = game.productionCode
        tvType.text = game.gameType
        tvExpansions.text = dbHandler.getExpansions(title).joinToString("\n")
        tvLocation.text = game.location
        tvComment.text = game.comment

    }


}