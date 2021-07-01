package com.example.boardgamecollector

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.core.database.getStringOrNull
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.core.database.getIntOrNull as getIntOrNull1

class MyDBHandler (context: Context, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int):
        SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION){

    companion object{
        private const val DATABASE_NAME = "boardGameDB.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_GAMES = "boardGames"
        const val COLUMN_TITLE = "title"
        const val COLUMN_ORIGINAL_TITLE = "originalTitle"
        const val COLUMN_PUBLICATION_YEAR = "year"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_ORDER_DATE = "orderDate"
        const val COLUMN_ADDED_DATE = "addedDate"
        const val COLUMN_PRICE = "price"
        const val COLUMN_SCD = "scd"
        const val COLUMN_CODE = "code"
        const val COLUMN_BGG_ID = "bggId"
        const val COLUMN_PRODUCTION_CODE = "productionCode"
        const val COLUMN_RANKING = "ranking"
        const val COLUMN_GAME_TYPE = "gameType"
        const val COLUMN_COMMENT = "comment"
        const val COLUMN_IMAGE = "image"
        const val COLUMN_LOCATION = "location"

        const val TABLE_DESIGNERS = "designers"
        const val COLUMN_DESIGNER_NAME = "name"

        const val TABLE_ARTISTS = "artists"
        const val COLUMN_ARTIST_NAME = "name"

        const val TABLE_DESIGNERS_GAME = "gameDesigners"
        const val COLUMN_DESIGNER_GAME_TITLE = "title"
        const val COLUMN_DESIGNER_GAME_NAME = "name"

        const val TABLE_ARTISTS_GAME = "gameArtists"
        const val COLUMN_ARTISTS_GAME_TITLE = "title"
        const val COLUMN_ARTISTS_GAME_NAME = "name"

        const val TABLE_LOCATIONS="location"
        const val COLUMN_LOCATION_NAME = "name"

        const val TABLE_EXPANSIONS = "expansions"
        const val COLUMN_EXPANSION_ID  = "_id"
        const val COLUMN_EXPANSION_TITLE = "title"
        const val COLUMN_EXPANSION_GAME = "parent"

        const val TABLE_RANKING = "ranking"
        const val COLUMN_RANKING_ID = "_id"
        const val COLUMN_RANKING_DATE = "date"
        const val COLUMN_RANKING_RANK = "rank"
        const val COLUMN_RANKING_TITLE = "title"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_GAMES = ("CREATE TABLE " +
                TABLE_GAMES + "(" +
                COLUMN_TITLE + " TEXT PRIMARY KEY, " +
                COLUMN_ORIGINAL_TITLE + " TEXT, " +
                COLUMN_PUBLICATION_YEAR + " INTEGER, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_ORDER_DATE + " TEXT, " +
                COLUMN_ADDED_DATE + " TEXT, " +
                COLUMN_PRICE + " TEXT, " +
                COLUMN_SCD + " TEXT, " +
                COLUMN_CODE + " TEXT, " +
                COLUMN_BGG_ID + " INTEGER, " +
                COLUMN_PRODUCTION_CODE + " TEXT, " +
                COLUMN_RANKING + " INTEGER, " +
                COLUMN_GAME_TYPE + " TEXT, " +
                COLUMN_COMMENT + " TEXT, " +
                COLUMN_IMAGE + " TEXT, " +
                COLUMN_LOCATION + " TEXT, " +
                "FOREIGN KEY (" + COLUMN_LOCATION + ") REFERENCES "+ TABLE_LOCATIONS + "(" + COLUMN_LOCATION_NAME + ")" +
                ")")
        db?.execSQL(CREATE_TABLE_GAMES)

        val CREATE_TABLE_DESIGNERS = ("CREATE TABLE " +
                TABLE_DESIGNERS + "(" +
                COLUMN_DESIGNER_NAME + " TEXT PRIMARY KEY" +
                ")")
        db?.execSQL(CREATE_TABLE_DESIGNERS)

        val CREATE_TABLE_ARTISTS = ("CREATE TABLE " +
                TABLE_ARTISTS + "(" +
                COLUMN_ARTIST_NAME + " TEXT PRIMARY KEY" +
                ")")
        db?.execSQL(CREATE_TABLE_ARTISTS)

        val CREATE_TABLE_DESIGNERS_GAME = ("CREATE TABLE " +
                TABLE_DESIGNERS_GAME + "(" +
                COLUMN_DESIGNER_GAME_TITLE + " TEXT, " +
                COLUMN_DESIGNER_GAME_NAME + " TEXT, " +
                "PRIMARY KEY (" + COLUMN_DESIGNER_GAME_TITLE + "," + COLUMN_DESIGNER_GAME_NAME + "), " +
                "FOREIGN KEY (" + COLUMN_DESIGNER_GAME_TITLE + ") REFERENCES " + TABLE_GAMES + "(" + COLUMN_TITLE + "), " +
                "FOREIGN KEY (" + COLUMN_DESIGNER_GAME_NAME + ") REFERENCES " + TABLE_DESIGNERS + "(" + COLUMN_DESIGNER_NAME + ")" +
                ")")
        db?.execSQL(CREATE_TABLE_DESIGNERS_GAME)

        val CREATE_TABLE_ARTISTS_GAME = ("CREATE TABLE " +
                TABLE_ARTISTS_GAME + "(" +
                COLUMN_ARTISTS_GAME_TITLE + " TEXT, " +
                COLUMN_ARTISTS_GAME_NAME + " TEXT, " +
                "PRIMARY KEY (" + COLUMN_ARTISTS_GAME_TITLE + "," + COLUMN_ARTISTS_GAME_NAME + "), " +
                "FOREIGN KEY (" + COLUMN_ARTISTS_GAME_TITLE + ") REFERENCES " + TABLE_GAMES + "(" + COLUMN_TITLE + "), " +
                "FOREIGN KEY (" + COLUMN_ARTISTS_GAME_NAME + ") REFERENCES " + TABLE_ARTISTS + "(" + COLUMN_ARTIST_NAME + ")" +
                ")")
        db?.execSQL(CREATE_TABLE_ARTISTS_GAME)

        val CREATE_TABLE_LOCATIONS = ("CREATE TABLE " +
                TABLE_LOCATIONS + "(" +
                COLUMN_LOCATION_NAME + " TEXT PRIMARY KEY" +
                ")")
        db?.execSQL(CREATE_TABLE_LOCATIONS)

        val CREATE_TABLE_EXPANSIONS = ("CREATE TABLE " +
                TABLE_EXPANSIONS + "(" +
                COLUMN_EXPANSION_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_EXPANSION_TITLE + " TEXT, " +
                COLUMN_EXPANSION_GAME + " TEXT, " +
                "FOREIGN KEY (" + COLUMN_EXPANSION_GAME + ") REFERENCES " + TABLE_GAMES + "(" + COLUMN_TITLE + ")" +
                ")")
        db?.execSQL(CREATE_TABLE_EXPANSIONS)

        val CREATE_TABLE_RANKING = ("CREATE TABLE " +
                TABLE_RANKING + "(" +
                COLUMN_RANKING_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_RANKING_DATE + " TEXT, " +
                COLUMN_RANKING_RANK + " TEXT, " +
                COLUMN_RANKING_TITLE + " TEXT, " +
                "FOREIGN KEY (" + COLUMN_RANKING_TITLE + ") REFERENCES " + TABLE_GAMES + "(" + COLUMN_TITLE + ")" +
                ")")
        db?.execSQL(CREATE_TABLE_RANKING)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_GAMES")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_DESIGNERS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ARTISTS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_DESIGNERS_GAME")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ARTISTS_GAME")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_LOCATIONS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_EXPANSIONS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_RANKING")
        onCreate(db)
    }

    fun addGame(game: BoardGame){
        val values = ContentValues()
        values.put(COLUMN_TITLE, game.title)
        values.put(COLUMN_ORIGINAL_TITLE, game.originalTitle)
        values.put(COLUMN_PUBLICATION_YEAR, game.publicationYear)
        values.put(COLUMN_DESCRIPTION, game.description)
        values.put(COLUMN_ORDER_DATE, game.orderDate)
        values.put(COLUMN_ADDED_DATE, game.addedDate)
        values.put(COLUMN_PRICE, game.price)
        values.put(COLUMN_SCD, game.scd)
        values.put(COLUMN_CODE, game.code)
        values.put(COLUMN_BGG_ID, game.bggId)
        values.put(COLUMN_PRODUCTION_CODE, game.productionCode)
        values.put(COLUMN_RANKING, game.ranking)
        values.put(COLUMN_GAME_TYPE, game.gameType)
        values.put(COLUMN_COMMENT, game.comment)
        values.put(COLUMN_IMAGE, game.image)
        values.put(COLUMN_LOCATION, game.location)

        val db = this.writableDatabase
        db.insert(TABLE_GAMES, null, values)
        db.close()
    }

    fun findGame(gameName: String): BoardGame{
        val query = "SELECT * FROM $TABLE_GAMES WHERE $COLUMN_TITLE = ?"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, arrayOf(gameName))
        var game = BoardGame()

        var title: String? = null
        var originalTitle: String? = null
        var publicationYear: Int? = null
        var description: String? = null
        var orderDate: String? = null
        var addedDate: String? = null
        var price: String? = null
        var scd: String? = null
        var code: String? = null
        var bggId: Int? = null
        var productionCode: String? = null
        var ranking: Int? = null
        var gameType: String? = null
        var comment: String? = null
        var image: String? = null
        var location: String? = null

        if (cursor.moveToFirst()){
            game.title = cursor.getStringOrNull(0)
            game.originalTitle = cursor.getStringOrNull(1)
            game.publicationYear = cursor.getInt(2)
            game.description = cursor.getStringOrNull(3)
            game.orderDate = cursor.getStringOrNull(4)
            game.addedDate = cursor.getStringOrNull(5)
            game.price = cursor.getStringOrNull(6)
            game.scd = cursor.getStringOrNull(7)
            game.code = cursor.getStringOrNull(8)
            game.bggId = cursor.getInt(9)
            game.productionCode = cursor.getStringOrNull(10)
            game.ranking = cursor.getInt(11)
            game.gameType = cursor.getStringOrNull(12)
            game.comment = cursor.getStringOrNull(13)
            game.image = cursor.getStringOrNull(14)
            game.location = cursor.getStringOrNull(15)
        }
        cursor.close()
//        val game = BoardGame(title, originalTitle, publicationYear, description, orderDate, addedDate,
//            price, scd, code, bggId, productionCode, ranking, gameType, comment, image, location)
        db.close()
        return game
    }

    fun deleteGame(gameName: String): Boolean {
        var result = false
        val query = "SELECT * FROM $TABLE_GAMES WHERE $COLUMN_TITLE = ?"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, arrayOf(gameName))
        if (cursor.moveToFirst()){
            db.delete(TABLE_GAMES, "$COLUMN_TITLE = ?", arrayOf(gameName))
            db.delete(TABLE_DESIGNERS_GAME, "$COLUMN_DESIGNER_GAME_TITLE = ?", arrayOf(gameName))
            db.delete(TABLE_ARTISTS_GAME, "$COLUMN_ARTISTS_GAME_TITLE = ?", arrayOf(gameName))
            db.delete(TABLE_EXPANSIONS, "$COLUMN_EXPANSION_GAME = ?", arrayOf(gameName))
            db.delete(TABLE_RANKING, "$COLUMN_RANKING_TITLE = ?", arrayOf(gameName))
            cursor.close()
            result = true
        }
        db.close()
        return result
    }

    fun editGame(){

    }

    @SuppressLint("Recycle")
    fun getAllGames(sortValue: String): MutableList<BoardGame>{
        var sortColumn: String? = null
        when(sortValue){
            "alfabetycznie" -> sortColumn = COLUMN_TITLE
            "data wydania" -> sortColumn = COLUMN_PUBLICATION_YEAR
            "pozycja rankingu" -> sortColumn = COLUMN_RANKING
        }
        val query = "SELECT * FROM $TABLE_GAMES ORDER BY $sortColumn"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        val returnList: MutableList<BoardGame> = ArrayList()
        while (cursor.moveToNext()){
            val game = BoardGame()
            game.title = cursor.getStringOrNull(0)
            game.publicationYear = cursor.getIntOrNull1(2)
            val desc = cursor.getStringOrNull(3)?.split(".")
            game.description = desc?.get(0)
            game.ranking = cursor.getIntOrNull1(11)
            game.image = cursor.getStringOrNull(14)
            returnList.add(game)
        }
        cursor.close()
        db.close()

        return returnList
    }

    fun getLocations(): MutableList<String>{
        val query = "SELECT * FROM $TABLE_LOCATIONS"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        val locations: MutableList<String> = ArrayList()
        while(cursor.moveToNext()){
            locations.add(cursor.getStringOrNull(0)!!)
        }
        cursor.close()
        db.close()

        return locations
    }

    @SuppressLint("Recycle")
    fun addDesigner(name: String, gameName: String){
        val query = "SELECT * FROM $TABLE_DESIGNERS WHERE $COLUMN_DESIGNER_NAME = ?"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, arrayOf(name))
        if (cursor.moveToFirst()){
            val values = ContentValues()
            values.put(COLUMN_DESIGNER_NAME, name)
            db.insert(TABLE_DESIGNERS, null, values)
        }
        cursor.close()

        val query2 = "SELECT * FROM $TABLE_DESIGNERS_GAME WHERE $COLUMN_DESIGNER_GAME_NAME = ? AND $COLUMN_DESIGNER_GAME_TITLE = ?"
        val cursor2 = db.rawQuery(query2, arrayOf(name, gameName))
        if (cursor2.moveToFirst()){
            val values2 = ContentValues()
            values2.put(COLUMN_DESIGNER_GAME_NAME, name)
            values2.put(COLUMN_DESIGNER_GAME_TITLE, gameName)
            db.insert(TABLE_DESIGNERS_GAME, null, values2)
        }
        cursor2.close()
        db.close()
    }

    fun addArtist(name: String, gameName: String){
        val query = "SELECT * FROM $TABLE_ARTISTS WHERE $COLUMN_ARTIST_NAME = ?"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, arrayOf(name))
        if (cursor.moveToFirst()){
            val values = ContentValues()
            values.put(COLUMN_ARTIST_NAME, name)
            db.insert(TABLE_ARTISTS, null, values)
        }
        cursor.close()

        val query2 = "SELECT * FROM $TABLE_ARTISTS_GAME WHERE $COLUMN_ARTISTS_GAME_NAME = ? AND $COLUMN_ARTISTS_GAME_TITLE = ?"
        val cursor2 = db.rawQuery(query2, arrayOf(name, gameName))
        if (cursor2.moveToFirst()){
            val values2 = ContentValues()
            values2.put(COLUMN_ARTISTS_GAME_NAME, name)
            values2.put(COLUMN_ARTISTS_GAME_TITLE, gameName)
            db.insert(TABLE_ARTISTS_GAME, null, values2)
        }
        cursor2.close()
        db.close()
    }

    fun addExpansion(name: String, gameName: String){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_EXPANSION_TITLE, name)
        values.put(COLUMN_EXPANSION_GAME, gameName)
        db.insert(TABLE_EXPANSIONS, null, values)


        db.close()
    }

    @SuppressLint("Recycle")
    fun isGameInDb(gameName: String): Boolean{
        var bool = false
        val query = "SELECT * FROM $TABLE_GAMES WHERE $COLUMN_TITLE = ?"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, arrayOf(gameName))
        if (cursor.moveToFirst()){
            bool = true
        }
        cursor.close()
        db.close()

        return bool
    }

    fun addGameRanking(rank: Int, gameName: String){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_RANKING_RANK, rank)
        values.put(COLUMN_RANKING_TITLE, gameName)
        val date = LocalDateTime.now()
        values.put(COLUMN_RANKING_DATE, date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
        db.insert(TABLE_RANKING, null, values)

        db.close()
    }

    @SuppressLint("Recycle")
    fun isLocationIdDb(locationName: String): Boolean{
        var bool = false
        val query = "SELECT * FROM $TABLE_LOCATIONS WHERE $COLUMN_LOCATION_NAME = ?"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, arrayOf(locationName))
        if (cursor.moveToFirst()){
            bool = true
        }
        cursor.close()
        db.close()

        return bool
    }

    fun addLocation(name: String){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_LOCATION_NAME, name)
        db.insert(TABLE_LOCATIONS, null, values)

        db.close()
    }

    fun getGamesByLocation(location: String): MutableList<String>{
        val query = "SELECT * FROM $TABLE_GAMES WHERE $COLUMN_LOCATION = ?"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, arrayOf(location))
        val games: MutableList<String> = ArrayList()
        while(cursor.moveToNext()){
            games.add(cursor.getStringOrNull(0)!!)
        }
        cursor.close()
        db.close()

        return games
    }

    fun editLocation(oldLoc: String, newLoc: String){
        val query = "SELECT * FROM $TABLE_GAMES WHERE $COLUMN_LOCATION = ?"
        val db = this.writableDatabase
        var values = ContentValues()
        values.put(COLUMN_LOCATION_NAME, newLoc)
        db.update(TABLE_LOCATIONS, values, "$COLUMN_LOCATION_NAME = ?", arrayOf(oldLoc))
        val cursor = db.rawQuery(query, arrayOf(oldLoc))
        while(cursor.moveToNext()){
            values = ContentValues()
            values.put(COLUMN_LOCATION, newLoc)
            db.update(TABLE_GAMES, values, "$COLUMN_TITLE = ?", arrayOf(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))))
        }
        cursor.close()
        db.close()
    }

    fun deleteLocation(name: String) {
        val db = this.writableDatabase
        db.delete(TABLE_LOCATIONS, "$COLUMN_LOCATION_NAME = ?", arrayOf(name))
        db.close()
    }

    fun isLocationEmpty(locationName: String): Boolean{
        var bool = true
        val query = "SELECT * FROM $TABLE_GAMES WHERE $COLUMN_LOCATION = ?"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, arrayOf(locationName))
        if (cursor.moveToFirst()){
            bool = false
        }
        cursor.close()
        db.close()

        return bool
    }

    fun getDesigners(name: String): MutableList<String> {
        val query = "SELECT * FROM $TABLE_DESIGNERS_GAME WHERE $COLUMN_DESIGNER_GAME_TITLE = ?"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, arrayOf(name))
        val designers: MutableList<String> = ArrayList()
        while(cursor.moveToNext()){
            designers.add(cursor.getStringOrNull(1)!!)
        }
        cursor.close()
        db.close()

        return designers
    }

    fun getArtists(name: String): MutableList<String> {
        val query = "SELECT * FROM $TABLE_ARTISTS_GAME WHERE $COLUMN_ARTISTS_GAME_TITLE = ?"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, arrayOf(name))
        val artists: MutableList<String> = ArrayList()
        while(cursor.moveToNext()){
            artists.add(cursor.getStringOrNull(1)!!)
        }
        cursor.close()
        db.close()

        return artists
    }

    fun getExpansions(name: String): MutableList<String> {
        val query = "SELECT * FROM $TABLE_EXPANSIONS WHERE $COLUMN_EXPANSION_GAME = ?"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, arrayOf(name))
        val expansions: MutableList<String> = ArrayList()
        while(cursor.moveToNext()){
            expansions.add(cursor.getStringOrNull(1)!!)
        }
        cursor.close()
        db.close()

        return expansions
    }

    fun updateDesigners(designers: String, gameName: String){
        val db = this.writableDatabase
        db.delete(TABLE_DESIGNERS_GAME, "$COLUMN_DESIGNER_GAME_TITLE = ?", arrayOf(gameName))
        for(i in designers.split("\n"))
            addDesigner(i, gameName)
        db.close()
    }

    fun updateArtists(artists: String, gameName: String){
        val db = this.writableDatabase
        db.delete(TABLE_ARTISTS_GAME, "$COLUMN_ARTISTS_GAME_TITLE = ?", arrayOf(gameName))
        for(i in artists.split("\n"))
            addArtist(i, gameName)
        db.close()
    }

    fun updateExpansions(expansions: String, gameName: String){
        val db = this.writableDatabase
        db.delete(TABLE_EXPANSIONS, "$COLUMN_EXPANSION_GAME = ?", arrayOf(gameName))
        for(i in expansions.split("\n"))
            addArtist(i, gameName)
        db.close()
    }

    fun updateGame(game: BoardGame){
        val query = "SELECT * FROM $TABLE_GAMES WHERE $COLUMN_TITLE = ?"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, arrayOf(game.title))

        val values = ContentValues()
        values.put(COLUMN_ORIGINAL_TITLE, game.originalTitle)
        values.put(COLUMN_PUBLICATION_YEAR, game.publicationYear)
        values.put(COLUMN_DESCRIPTION, game.description)
        values.put(COLUMN_ORDER_DATE, game.orderDate)
        values.put(COLUMN_ADDED_DATE, game.addedDate)
        values.put(COLUMN_PRICE, game.price)
        values.put(COLUMN_SCD, game.scd)
        values.put(COLUMN_CODE, game.code)
        values.put(COLUMN_BGG_ID, game.bggId)
        values.put(COLUMN_PRODUCTION_CODE, game.productionCode)
        values.put(COLUMN_RANKING, game.ranking)
        values.put(COLUMN_GAME_TYPE, game.gameType)
        values.put(COLUMN_COMMENT, game.comment)
        values.put(COLUMN_IMAGE, game.image)
        values.put(COLUMN_LOCATION, game.location)

        if (cursor.moveToFirst()){
            db.update(TABLE_GAMES, values, "$COLUMN_TITLE = ?", arrayOf(game.title))
        }

        cursor.close()
        db.close()
    }

    fun getRanks(gameName: String): String{

        val query = "SELECT * FROM $TABLE_RANKING WHERE $COLUMN_RANKING_TITLE = ? ORDER BY $COLUMN_RANKING_DATE DESC"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, arrayOf(gameName))
        val returnList: MutableList<String> = ArrayList()
        val info = "Data         | Pozycja rankingu"
        returnList.add(info)
        while (cursor.moveToNext()){
            val rank = cursor.getStringOrNull(1)!!.substring(10) + " | " + cursor.getStringOrNull(2)

            returnList.add(rank)
        }
        cursor.close()
        db.close()

        return returnList.joinToString("\n")

    }
}