package com.example.boardgamecollector

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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
import javax.xml.parsers.DocumentBuilderFactory

class ImportBggActivity : AppCompatActivity() {

    var gamesId: MutableList<String> = ArrayList()
    val dbHandler = MyDBHandler(this, null, null, 1)
    var gameDesigners: MutableList<String> = ArrayList()
    var gameArtists: MutableList<String> = ArrayList()
    var gameExpansions: MutableList<String> = ArrayList()

    var idUpdateList: MutableList<Int> = ArrayList()
    var rankUpdateList: MutableList<Int> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_import_bgg)
    }

    fun onImportBggButton(v: View){
        val importBggButton: Button = findViewById(R.id.buttonImportBgg)
        val gameInfo = GameInfoAsync()
        gameInfo.execute()
    }

    fun onImportRanksButton(v: View){
        val rank = RankingDownloader()
        rank.execute()

    }


    fun loadData(){
        val filename = "collection.xml"
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
                        gamesId.add(elem.getAttribute("objectid"))

                    }
                }
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
            val gameDetails = GameDetailsAsync()
            gameDetails.execute()
        }

        override fun doInBackground(vararg p0: String?): String {
            try{
                val nameEditText: EditText = findViewById(R.id.editTextBggNickname)
                val name = nameEditText.text
                val url = URL("https://www.boardgamegeek.com/xmlapi2/collection?username=$name")
                val connection = url.openConnection()
                connection.connect()
                val lengthOfFile = connection.contentLength
                val isStream = url.openStream()
                val testDirectory = File("$filesDir/XML")
                if(!testDirectory.exists()) testDirectory.mkdir()
                val fos = FileOutputStream("$testDirectory/collection.xml")
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


    fun loadDetails(): BoardGame{
        gameDesigners = ArrayList()
        gameArtists = ArrayList()
        gameExpansions = ArrayList()
        val boardGame = BoardGame()

        val filename = "collection-details.xml"
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
                                            boardGame.originalTitle = node.getAttribute("value")
                                        }
                                        boardGame.title = boardGame.originalTitle
                                    }
                                    "thumbnail" -> {
                                        boardGame.image = node.textContent
                                    }
                                    //TODO description?
                                    "description" -> {
                                        boardGame.description = node.textContent
                                    }
                                    "yearpublished" -> {
                                        boardGame.publicationYear = node.getAttribute("value").toInt()
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
                            "boardgame" -> boardGame.gameType = "podstawowa"
                            "boardgameexpansion" -> boardGame.gameType = "dodatek"
                            else -> boardGame.gameType = "mieszana"
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
                                                boardGame.ranking = node.getAttribute("value").toInt()
                                            else
                                                boardGame.ranking = 0
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
        return boardGame
    }

    fun addGameFromBggCollection(selectGame: BoardGame){
        val dbHandler = MyDBHandler(this, null, null, 1)
        if (dbHandler.isGameInDb(selectGame.title.toString())){
            Log.i("Existing Game", selectGame.title.toString())
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

    fun makeToast(){
        if(gamesId.isNotEmpty())
            Toast.makeText(this, "Zakonczono powodzeniem", Toast.LENGTH_LONG).show()
        else
            Toast.makeText(this, "Brak gier do dodania", Toast.LENGTH_LONG).show()
    }

    @SuppressLint("StaticFieldLeak")
    private inner class GameDetailsAsync: AsyncTask<String, Int, String>() {

        override fun onPreExecute(){
            super.onPreExecute()

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            makeToast()
        }

        override fun doInBackground(vararg p0: String?): String {
            try{
                for (id in gamesId){

                    val url = URL("https://www.boardgamegeek.com/xmlapi2/thing?id=$id&stats=1")
                    val connection = url.openConnection()
                    connection.connect()
                    val lengthOfFile = connection.contentLength
                    val isStream = url.openStream()
                    val testDirectory = File("$filesDir/XML")
                    if(!testDirectory.exists()) testDirectory.mkdir()
                    val fos = FileOutputStream("$testDirectory/collection-details.xml")
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

                    val game = loadDetails()
                    game.bggId = id.toInt()
                    if (dbHandler.isGameInDb(game.title.toString())){
                        Log.i("Existing Game", game.title.toString())
                    }
                    else{
                        dbHandler.addGame(game)
                        dbHandler.addGameRanking(game.ranking!!, game.title.toString())
                        for (i in gameDesigners){
                            dbHandler.addDesigner(i, game.title.toString())
                        }
                        for (i in gameArtists){
                            dbHandler.addArtist(i, game.title.toString())
                        }
                        for (i in gameExpansions){
                            dbHandler.addExpansion(i, game.title.toString())
                        }
                    }

                }
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

    fun updateRank() {
        val inDir = File("$filesDir/XML")
        idUpdateList = ArrayList()
        rankUpdateList = ArrayList()
        if (!inDir.exists()) inDir.mkdir()
        if (inDir.exists()) {
            val file = File(inDir, "collection-stats.xml")
            if (file.exists()) {
                val xmlDoc: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
                        file
                )
                xmlDoc.documentElement.normalize()
                val items: NodeList = xmlDoc.getElementsByTagName("item")
                val ranks: NodeList = xmlDoc.getElementsByTagName("ranks")
                for (i in 0 until items.length) {
                    val itemNode: Node = items.item(i)
                    if (itemNode.nodeType == Node.ELEMENT_NODE) {
                        val elem = itemNode as Element
                        idUpdateList.add(elem.getAttribute("objectid").toInt())
                    }
                }
                for (i in 0 until ranks.length) {
                    val ranksNode: Node = ranks.item(i)
                    if (ranksNode.nodeType == Node.ELEMENT_NODE) {
                        val elem = ranksNode as Element
                        val children = elem.childNodes
                        for (j in 0 until children.length) {
                            val node = children.item(j)
                            if (node is Element) {
                                when (node.nodeName) {
                                    "rank" -> {
                                        if (node.getAttribute("name") == "boardgame")
                                            if (node.getAttribute("value") != "Not Ranked")
                                                rankUpdateList.add(node.getAttribute("value").toInt())
                                            else
                                                rankUpdateList.add(0)
                                    }
                                }
                            }
                        }
                    }
                }
                for (i in 0 until idUpdateList.size)
                    if(dbHandler.isGameInDbByBggId(idUpdateList[i])){
                        dbHandler.updateGameRank(idUpdateList[i],rankUpdateList[i])
                        dbHandler.addRank(rankUpdateList[i], dbHandler.findGameByBggId(idUpdateList[i]))
                    }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private inner class RankingDownloader : AsyncTask<String, Int, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            updateRank()
            val rd2 = RankingDownloader2()
            rd2.execute()
        }

        override fun doInBackground(vararg params: String?): String {
            try {
                val textInput: EditText = findViewById(R.id.editTextBggNickname)
                val username = textInput.text
                val url = URL("https://www.boardgamegeek.com/xmlapi2/collection?username=$username&stats=1")
                val connection = url.openConnection()
                connection.connect()
                val lengthOfFile = connection.contentLength
                val isStream = url.openStream()
                val directory = File("$filesDir/XML")
                if (!directory.exists()) directory.mkdir()
                val fos = FileOutputStream("$directory/collection-stats.xml")
                val data = ByteArray(1024)
                var count = 0
                var total: Long = 0
                var progress = 0
                count = isStream.read(data)
                while (count != -1) {
                    total += count.toLong()
                    val progressTemp = total.toInt() * 100 / lengthOfFile
                    if (progressTemp % 10 == 0 && progress != progressTemp)
                        progress = progressTemp
                    fos.write(data, 0, count)
                    count = isStream.read(data)
                }
                isStream.close()
                fos.close()
            } catch (e: MalformedURLException) {
                return "Malformed URL"
            } catch (e: FileNotFoundException) {
                return "File not found"
            } catch (e: IOException) {
                return "IO Exception"
            }
            return "success"
        }
    }

    fun updateRank2() {
        gameArtists = ArrayList()
        gameDesigners = ArrayList()
        gameExpansions = ArrayList()
        var rank: Int = 0
        var id: Int = 0
        val inDir = File("$filesDir/XML")
        if (!inDir.exists()) inDir.mkdir()
        if (inDir.exists()) {
            val file = File(inDir, "collection-details.xml")
            if (file.exists()) {
                val xmlDoc: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
                        file
                )
                xmlDoc.documentElement.normalize()
                val items: NodeList = xmlDoc.getElementsByTagName("item")
                val ranks: NodeList = xmlDoc.getElementsByTagName("ranks")
                for (i in 0 until items.length) {
                    val itemNode: Node = items.item(i)
                    if (itemNode.nodeType == Node.ELEMENT_NODE) {
                        val elem = itemNode as Element
                        id = elem.getAttribute("id").toInt()
                    }
                }
                for (i in 0 until ranks.length) {
                    val ranksNode: Node = ranks.item(i)
                    if (ranksNode.nodeType == Node.ELEMENT_NODE) {
                        val elem = ranksNode as Element
                        val children = elem.childNodes
                        for (j in 0 until children.length) {
                            val node = children.item(j)
                            if (node is Element) {
                                when (node.nodeName) {
                                    "rank" -> {
                                        if (node.getAttribute("name") == "boardgame"){
                                            if (node.getAttribute("value") != "Not Ranked")
                                                rank = node.getAttribute("value").toInt()
                                            else
                                                rank = 0
                                        }


                                    }
                                }
                            }
                        }
                    }
                }
                dbHandler.updateGameRank(id,rank)
                dbHandler.addRank(rank,dbHandler.findGameByBggId(id))
            }
        }
    }

    private fun makeToast2(){
        Toast.makeText(this, "Zaktualizowano rankingi", Toast.LENGTH_LONG).show()
    }


    @SuppressLint("StaticFieldLeak")
    private inner class RankingDownloader2 : AsyncTask<String, Int, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            updateRank2()
            makeToast2()
        }

        override fun doInBackground(vararg params: String?): String {
            try {
                for (id in dbHandler.getGamesBggIds())
                    if(id !in idUpdateList){
                        val url = URL("https://www.boardgamegeek.com/xmlapi2/thing?id=$id&stats=1")
                        val connection = url.openConnection()
                        connection.connect()
                        val lengthOfFile = connection.contentLength
                        val isStream = url.openStream()
                        val directory = File("$filesDir/XML")
                        if (!directory.exists()) directory.mkdir()
                        val fos = FileOutputStream("$directory/collection-details.xml")
                        val data = ByteArray(1024)
                        var count = 0
                        var total: Long = 0
                        var progress = 0
                        count = isStream.read(data)
                        while (count != -1) {
                            total += count.toLong()
                            val progressTemp = total.toInt() * 100 / lengthOfFile
                            if (progressTemp % 10 == 0 && progress != progressTemp)
                                progress = progressTemp
                            fos.write(data, 0, count)
                            count = isStream.read(data)
                        }
                        isStream.close()
                        fos.close()
                    }
            } catch (e: MalformedURLException) {
                return "Malformed URL"
            } catch (e: FileNotFoundException) {
                return "File not found"
            } catch (e: IOException) {
                return "IO Exception"
            }
            return "success"
        }

    }

}