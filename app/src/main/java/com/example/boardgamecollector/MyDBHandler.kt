package com.example.boardgamecollector

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

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

        const val TABLE_OSOBY = "osoby"
        const val COLUMN_NAME = "name"

        const val TABLE_ARTIST_RELATION ="artist_relation"
        const val COLUMN_GAMEID = "game_id"
        const val COLUMN_ARTISTID = "artist_id"

        const val TABLE_DESIGNER_RELATION ="designer_relation"
        const val COLUMN_DESIGNERID = "designer_id"

        const val TABLE_LOCATION="location"
        const val TABLE_DODATKI="dodatki"
        const val TABLE_HISTORY="history"
        const val COLUMN_DATE = "data"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_PRODUCTS_TABLE = ("CREATE TABLE " +
                TABLE_PRODUCTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_PRODUCTNAME
                + " TEXT," + COLUMN_QUANTITY + " INTEGER" + ")")
        db?.execSQL(CREATE_PRODUCTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS)
        onCreate(db)
    }

    fun addProduct(product: Product){
        val values = ContentValues()
        values.put(COLUMN_PRODUCTNAME, product.productName)
        values.put(COLUMN_QUANTITY, product.quantity)
        val db = this.writableDatabase
        db.insert(TABLE_PRODUCTS, null, values)
        db.close()
    }

    fun findProduct(productname: String): Product?{
        val query = "SELECT * FROM $TABLE_PRODUCTS WHERE $COLUMN_PRODUCTNAME LIKE \"$productname\""
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        var product: Product? = null

        if (cursor.moveToFirst()){
            val id = Integer.parseInt(cursor.getString(0))
            val name = cursor.getString(1)
            val quantity = cursor.getInt(2)
            product = Product(id, name, quantity)
            cursor.close()
        }
        db.close()
        return product
    }

    fun deleteProduct(productname: String): Boolean{
        var result = false
        val query = "SELECT * FROM $TABLE_PRODUCTS WHERE $COLUMN_PRODUCTNAME LIKE \"$productname\""
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()){
            val id = cursor.getInt(0)
            db.delete(TABLE_PRODUCTS, COLUMN_ID+" = ?", arrayOf(id.toString()))
            cursor.close()
            result = true
        }
        db.close()
        return result
    }
}