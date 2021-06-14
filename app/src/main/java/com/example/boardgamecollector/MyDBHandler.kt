package com.example.boardgamecollector

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.database.getStringOrNull

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
                COLUMN_LOCATION + " TEXT FOREIGN KEY REFERENCES "+ TABLE_LOCATIONS + "(" + COLUMN_LOCATION_NAME + ")" +
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
                COLUMN_EXPANSION_GAME + " TEXT FOREIGN KEY REFERENCES "+ TABLE_GAMES + "(" + COLUMN_TITLE + ")" +
                ")")
        db?.execSQL(CREATE_TABLE_EXPANSIONS)

        val CREATE_TABLE_RANKING = ("CREATE TABLE " +
                TABLE_RANKING + "(" +
                COLUMN_RANKING_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_RANKING_DATE + " TEXT, " +
                COLUMN_RANKING_RANK + " TEXT, " +
                COLUMN_RANKING_TITLE + " TEXT FOREIGN KEY REFERENCES "+ TABLE_GAMES + "(" + COLUMN_TITLE + ")" +
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

    fun findGame(gameName: String): BoardGame?{
        val query = "SELECT * FROM $TABLE_GAMES WHERE $COLUMN_TITLE = $gameName"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        var game: BoardGame? = null

        if (cursor.moveToFirst()){
            val title = cursor.getStringOrNull(0)
            val originalTitle = cursor.getStringOrNull(1)
            val publicationYear = cursor.getInt(3)
            val description = cursor.getStringOrNull(4)
            val orderDate = cursor.getStringOrNull(5)
            val addedDate = cursor.getStringOrNull(6)
            val price = cursor.getStringOrNull(7)
            val scd = cursor.getStringOrNull(8)
            val code = cursor.getStringOrNull(9)
            val bggId = cursor.getInt(10)
            val productionCode = cursor.getStringOrNull(1)
            val ranking = cursor.getInt(12)
            val gameType = cursor.getStringOrNull(13)
            val comment = cursor.getStringOrNull(14)
            val image = cursor.getStringOrNull(15)
            val location = cursor.getStringOrNull(16)

            game = BoardGame(title, originalTitle, publicationYear, description, orderDate, addedDate,
                    price, scd, code, bggId, productionCode, ranking, gameType, comment, image, location)
            cursor.close()
        }
        db.close()
        return game
    }

    fun deleteGame(gameName: String): Boolean{
        var result = false
        val query = "SELECT * FROM $TABLE_GAMES WHERE $COLUMN_TITLE = $gameName"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
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

    fun getAllGames(){

    }
}