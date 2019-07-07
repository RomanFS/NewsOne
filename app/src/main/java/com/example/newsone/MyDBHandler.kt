package com.example.newsone

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDBHandler(context: Context, name: String?,
                  factory: SQLiteDatabase.CursorFactory?, version: Int) :
    SQLiteOpenHelper(context, DATABASE_NAME,
        factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val tableData = "(_id INTEGER PRIMARY KEY NOT NULL, url TEXT, title TEXT, " +
                "descrip TEXT, copyright TEXT, image_url TEXT, source TEXT, published_date TEXT, byline TEXT)"

        val CREATE_EMAILED_TABLE = ("CREATE TABLE $TABLE_EMAILED$tableData")
        val CREATE_VIEWED_TABLE = ("CREATE TABLE $TABLE_VIEWED$tableData")
        val CREATE_SHARED_TABLE = ("CREATE TABLE $TABLE_SHARED$tableData")
        val CREATE_FAVOURITE_TABLE = ("CREATE TABLE $TABLE_FAVOURITE$tableData")

        db.execSQL(CREATE_EMAILED_TABLE)
        db.execSQL(CREATE_VIEWED_TABLE)
        db.execSQL(CREATE_SHARED_TABLE)
        db.execSQL(CREATE_FAVOURITE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int,
                           newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EMAILED")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_VIEWED")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SHARED")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FAVOURITE")
        onCreate(db)
    }

    fun addNews(product: NewsObject, tableName: String) {
        val values = ContentValues()
        values.put(url, product.url)
        values.put(title, product.title)
        values.put(descrip, product.descrip)
        values.put(copyright, product.copyright)
        values.put(imageUrl, product.imageUrl)
        values.put(source, product.source)
        values.put(publishedDate, product.publishedDate)
        values.put(byline, product.byline)

        val db = this.writableDatabase

        db.insert(tableName, null, values)
        db.close()
    }

    fun findNews(tableName: String,newsTitle: String): NewsObject? {
        val query = "SELECT * FROM $tableName WHERE title =  \"$newsTitle\""
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        var news: NewsObject? = null

        if (cursor.moveToFirst()) {
            cursor.moveToFirst()

            val id = Integer.parseInt(cursor.getString(0))
            val name = cursor.getString(1)
            val quantity = Integer.parseInt(cursor.getString(2))
            news = NewsObject(url, title, descrip, copyright, imageUrl, source, publishedDate, byline)
            cursor.close()
        }

        db.close()
        return news
    }

    fun deleteNews(tableName: String,newsTitle: String): Boolean {
        var result = false
        val query = "SELECT * FROM $tableName WHERE title = \"$newsTitle\""
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            val id = Integer.parseInt(cursor.getString(0))
            db.delete(
                tableName, "$COLUMN_ID = ?",
                arrayOf(id.toString())
            )
            cursor.close()
            result = true
        }
        db.close()
        return result
    }

    companion object {
        private const val COLUMN_ID = "_ID"
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "newsData.db"
        const val TABLE_EMAILED = "emailed"
        const val TABLE_VIEWED = "viewed"
        const val TABLE_SHARED = "shared"
        const val TABLE_FAVOURITE = "shared"
        const val url = "url"
        const val title = "title"
        const val descrip = "descrip"
        const val copyright = "copyright"
        const val imageUrl = "image_url"
        const val source = "source"
        const val publishedDate = "published_date"
        const val byline = "byline"
    }
}
