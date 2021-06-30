package com.example.boardgamecollector

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.time.LocalDate
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.collections.ArrayList

class AddGameActivity : AppCompatActivity() {

    var gamesId: MutableList<String> = ArrayList()
    var gamesTitle: MutableList<String> = ArrayList()
    var gameDesigners: MutableList<String> = ArrayList()
    var gameArtists: MutableList<String> = ArrayList()
    var gameExpansions: MutableList<String> = ArrayList()

    var selectGame = BoardGame()
    var inputGame = BoardGame()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_game)
    }

    fun onSearchButton(v: View){
        val searchGameButton: Button = findViewById(R.id.szukajBtn)
        val gameInfo = GameInfoAsync()
        if (searchGameButton.text.isNotEmpty())
            gameInfo.execute()
    }

    fun onAddNewGameButton(v: View){
        val detailsScrollView: ScrollView = findViewById(R.id.DetailsScrollView)
        val titleEditText: EditText = findViewById(R.id.editTextTitle)
        val originalTitleEditText: EditText = findViewById(R.id.editTextOriginalTitle)
        val designersEditText: EditText = findViewById(R.id.editTextDesigners)
        val artistsEditText: EditText = findViewById(R.id.editTextArtists)
        val descriptionEditText: EditText = findViewById(R.id.editTextDescription)
        val priceEditText: EditText = findViewById(R.id.editTextPrice)
        val scdEditText: EditText = findViewById(R.id.editTextSCD)
        val codeEditText: EditText = findViewById(R.id.editTextCode)
        val productionCodeEditText: EditText = findViewById(R.id.editTextProductionCod)
        val expansionsEditText: EditText = findViewById(R.id.editTextExpansions)
        val commentEditText: EditText = findViewById(R.id.editTextComment)
        val dbHandler = MyDBHandler(this, null, null, 1)

        inputGame.title = titleEditText.text.toString()
        inputGame.originalTitle = originalTitleEditText.text.toString()
        inputGame.description = descriptionEditText.text.toString()
        inputGame.price = priceEditText.text.toString()
        inputGame.scd = scdEditText.text.toString()
        inputGame.code = codeEditText.text.toString()
        inputGame.productionCode = productionCodeEditText.text.toString()
        inputGame.ranking = 0
        inputGame.comment = commentEditText.text.toString()

        for (i in designersEditText.text?.split("\n")!!)
            dbHandler.addDesigner(i, inputGame.title.toString())
        for (i in artistsEditText.text?.split("\n")!!)
            dbHandler.addArtist(i, inputGame.title.toString())
        for (i in expansionsEditText.text?.split("\n")!!)
            dbHandler.addExpansion(i, inputGame.title.toString())

        when {
            inputGame.title.isNullOrEmpty() -> Toast.makeText(this, "Dodaj tytuł", Toast.LENGTH_LONG).show()
            dbHandler.isGameInDb(inputGame.title.toString()) -> {
                Toast.makeText(this, "Gra juz istnieje", Toast.LENGTH_LONG).show()
            }
            else -> {
                dbHandler.addGame(inputGame)
                detailsScrollView.visibility = View.GONE
            }
        }

    }

    fun onAddDetailsButton(v: View){
        val scrollViewBgg: ScrollView = findViewById(R.id.ScrollViewBgg)
        val scrollViewDetails: ScrollView = findViewById(R.id.DetailsScrollView)
        scrollViewBgg.visibility = View.GONE
        scrollViewDetails.visibility = View.VISIBLE
        inputGame = BoardGame()
        val dbHandler = MyDBHandler(this, null, null, 1)

        val yearNumberPicker: NumberPicker = findViewById(R.id.NumberPickerPublicationYear)
        val orderDateDatePicker: DatePicker = findViewById(R.id.DatePickerOrderDate)
        val addedDateDatePicker: DatePicker = findViewById(R.id.DatePickerAddedDate)
        val typeSpinner: Spinner = findViewById(R.id.spinnerType)
        val locationSpinner: Spinner = findViewById(R.id.spinnerLocation)

        yearNumberPicker.value = 2021
        yearNumberPicker.minValue = 1900
        yearNumberPicker.maxValue = 2100
        yearNumberPicker.wrapSelectorWheel = false
        yearNumberPicker.setOnValueChangedListener { numberPicker, i, value ->
            inputGame.publicationYear = value
        }

        orderDateDatePicker.setOnDateChangedListener { datePicker, year, month, day ->
            inputGame.orderDate = LocalDate.of(year, month + 1, day).toString()
        }

        addedDateDatePicker.setOnDateChangedListener { datePicker, year, month, day ->
            inputGame.addedDate = LocalDate.of(year, month + 1, day).toString()
        }

        val typeList: MutableList<String> = ArrayList()
        typeList.add("podstawowa")
        typeList.add("dodatek")
        typeList.add("mieszana")
        val typeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, typeList)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        typeSpinner.adapter = typeAdapter
        typeSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                inputGame.gameType = typeSpinner.selectedItem.toString()
            }
        }

        val locationList = dbHandler.getLocations()
        val locationAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, locationList)
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        locationSpinner.adapter = locationAdapter
        locationSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                inputGame.location = locationSpinner.selectedItem.toString()
            }
        }

    }

    fun loadData(){
        gamesId = ArrayList()
        gamesTitle = ArrayList()

        val filename = "games.xml"
        val path = filesDir
        val inDir = File(path, "XML")

        if (inDir.exists()){
            val file = File(inDir, filename)
            if (file.exists()){
                val xmlDoc: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)

                xmlDoc.documentElement.normalize()

                val items: NodeList = xmlDoc.getElementsByTagName("item")

                for (i in 0..items.length-1){
                    val itemNode: Node = items.item(i)
                    if (itemNode.nodeType == Node.ELEMENT_NODE){
                        val elem = itemNode as Element
                        val children = elem.childNodes
                        gamesId.add(elem.getAttribute("id"))
                        var currentId: String? = null
                        var currentTitle: String? = null

                        for (j in 0..children.length-1){
                            val node = children.item(j)
                            if (node is Element){
                                when (node.nodeName){
                                    "name" -> {
                                        currentTitle = node.textContent
                                        gamesTitle.add(node.getAttribute("value"))
                                    }
                                }
                            }
                        }

//                        if (currentId != null && currentTitle != null){
//                            (gamesId as ArrayList<String>).add(currentId)
//                            (gamesTitle as ArrayList<String>).add(currentTitle)
//                        }
                    }
                }
            }
        }
    }

    fun showGames(){
        val scrollViewBgg: ScrollView = findViewById(R.id.ScrollViewBgg)
        val scrollViewDetails: ScrollView = findViewById(R.id.DetailsScrollView)
        val tableLayoutBgg: TableLayout = findViewById(R.id.tableLayoutBgg)

        scrollViewBgg.visibility = View.VISIBLE
        tableLayoutBgg.removeAllViewsInLayout()
        tableLayoutBgg.visibility = View.VISIBLE
        scrollViewDetails.visibility = View.GONE

        for (i in 0..gamesTitle.size-1){
            val tableRow = TableRow(this)
            val textViewTitle = TextView(this)
            textViewTitle.textSize = 18F
            textViewTitle.text = gamesTitle[i]
            tableRow.addView(textViewTitle)
            tableLayoutBgg.addView(tableRow)

            tableRow.setOnClickListener {
                selectGame.title = gamesTitle[i]
                selectGame.bggId = gamesId[i].toInt()
                val gameDetails = GameDetailsAsync()
                gameDetails.execute()
                scrollViewBgg.visibility = View.GONE
                tableLayoutBgg.removeAllViewsInLayout()
                tableLayoutBgg.visibility = View.GONE
            }
        }

    }

    @SuppressLint("StaticFieldLeak")
    private inner class GameInfoAsync: AsyncTask<String, Int, String>() {

        override fun onPreExecute(){
            super.onPreExecute()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            loadData()
            showGames()
        }

        override fun doInBackground(vararg p0: String?): String {
            try{
                val gameTitle: EditText = findViewById(R.id.editTextGameTitle)
                val title = gameTitle.text
                val url = URL("https://www.boardgamegeek.com/xmlapi2/search?query=$title&type=boardgame")
                val connection = url.openConnection()
                connection.connect()
                val lengthOfFile = connection.contentLength
                val isStream = url.openStream()
                val testDirectory = File("$filesDir/XML")
                if(!testDirectory.exists()) testDirectory.mkdir()
                val fos = FileOutputStream("$testDirectory/games.xml")
                val data = ByteArray(1024)
                var count = 0
                var total: Long = 0
                var progress = 0
                count = isStream.read(data)
                while(count != -1){
                    total += count.toLong()
                    val progressTemp = total.toInt() * 100 / lengthOfFile
                    if (progressTemp % 10 == 0 && progress != progressTemp)
                        progress = progressTemp
                    fos.write(data, 0, count)
                    count = isStream.read(data)
                }
                isStream.close()
                fos.close()
            }catch (e: MalformedURLException){
            return "Malformed URL"
        }catch (e: FileNotFoundException){
            return "File not found"
        }catch (e: IOException){
            return "IO Exception"
        }
        return "success"
        }
    }


    fun loadDetails(){
        val filename = "games-details.xml"
        val path = filesDir
        val inDir = File(path, "XML")

        if (inDir.exists()){
            val file = File(inDir, filename)
            if (file.exists()){
                val xmlDoc: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)

                xmlDoc.documentElement.normalize()

                val items: NodeList = xmlDoc.getElementsByTagName("item")

                for (i in 0..items.length-1){
                    val itemNode: Node = items.item(i)
                    if (itemNode.nodeType == Node.ELEMENT_NODE){
                        val elem = itemNode as Element
                        val children = elem.childNodes

                        for (j in 0..children.length-1){
                            val node = children.item(j)
                            if (node is Element){
                                when (node.nodeName){
                                    "name" -> {
                                        if (node.getAttribute("type") == "primary"){
                                            selectGame.originalTitle = node.getAttribute("value")
                                        }
                                    }
                                    "thumbnail" -> {
                                        selectGame.image = node.textContent
                                    }
                                    //TODO description?
                                    "description" -> {
                                        selectGame.description = node.textContent
                                    }
                                    "yearpublished" -> {
                                        selectGame.publicationYear = node.getAttribute("value").toInt()
                                    }
                                    "link" -> {
                                        when {
                                            node.getAttribute("type") == "boardgamedesigner" -> gameDesigners.add(node.getAttribute("value"))
                                            node.getAttribute("type") == "boardgameartist" -> gameArtists.add(node.getAttribute("value"))
                                            node.getAttribute("type") == "boardgameexpansion" -> gameExpansions.add(node.getAttribute("value"))
                                        }
                                    }

                                }
                            }
                        }
                        when(elem.getAttribute("type")){
                            "boardgame" -> selectGame.gameType = "podstawowa"
                            "boardgameexpansion" -> selectGame.gameType = "dodatek"
                            else -> selectGame.gameType = "mieszana"
                        }

                    }
                }
                val rankings: NodeList = xmlDoc.getElementsByTagName("ranks")
                for (i in 0..rankings.length-1){
                    val itemNode: Node = rankings.item(i)
                    if (itemNode.nodeType == Node.ELEMENT_NODE){
                        val elem = itemNode as Element
                        val children = elem.childNodes

                        for (j in 0..children.length-1){
                            val node = children.item(j)
                            if (node is Element){
                                when (node.nodeName){
                                    "rank" -> {
                                        if (node.getAttribute("name") == "boardgame") {
                                            if (node.getAttribute("value") != "Not Ranked")
                                                selectGame.ranking = node.getAttribute("value").toInt()
                                            else
                                                selectGame.ranking = 0
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    fun addGameFromBgg(){
        val dbHandler = MyDBHandler(this, null, null, 1)
        if (dbHandler.isGameInDb(selectGame.title.toString())){
            Toast.makeText(this, "Gra juz istnieje", Toast.LENGTH_LONG).show()
        }
        else{
            dbHandler.addGame(selectGame)
            dbHandler.addGameRanking(selectGame.ranking!!, selectGame.title.toString())
            for (i in gameDesigners){
                dbHandler.addDesigner(i, selectGame.title.toString())
            }
            for (i in gameArtists){
                dbHandler.addArtist(i, selectGame.title.toString())
            }
            for (i in gameExpansions){
                dbHandler.addExpansion(i, selectGame.title.toString())
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private inner class GameDetailsAsync: AsyncTask<String, Int, String>() {

        override fun onPreExecute(){
            super.onPreExecute()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            loadDetails()
            addGameFromBgg()
        }

        override fun doInBackground(vararg p0: String?): String {
            try{
                val title = selectGame.bggId
                val url = URL("https://www.boardgamegeek.com/xmlapi2/thing?id=$title&stats=1")
                val connection = url.openConnection()
                connection.connect()
                val lengthOfFile = connection.contentLength
                val isStream = url.openStream()
                val testDirectory = File("$filesDir/XML")
                if(!testDirectory.exists()) testDirectory.mkdir()
                val fos = FileOutputStream("$testDirectory/games-details.xml")
                val data = ByteArray(1024)
                var count = 0
                var total: Long = 0
                var progress = 0
                count = isStream.read(data)
                while(count != -1){
                    total += count.toLong()
                    val progressTemp = total.toInt() * 100 / lengthOfFile
                    if (progressTemp % 10 == 0 && progress != progressTemp)
                        progress = progressTemp
                    fos.write(data, 0, count)
                    count = isStream.read(data)
                }
                isStream.close()
                fos.close()
            }catch (e: MalformedURLException){
                return "Malformed URL"
            }catch (e: FileNotFoundException){
                return "File not found"
            }catch (e: IOException){
                return "IO Exception"
            }
            return "success"
        }
    }



}