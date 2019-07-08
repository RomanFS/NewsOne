package com.example.newsone

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class MyDBHandler(context: Context, name: String?,
                  factory: SQLiteDatabase.CursorFactory?, version: Int) :
    SQLiteOpenHelper(context, DATABASE_NAME,
        factory, DATABASE_VERSION) {
    private val TAG = "MyDBHandler"

    override fun onCreate(db: SQLiteDatabase) {
        val tableData = "(_id INTEGER PRIMARY KEY NOT NULL, url TEXT, title TEXT, " +
                "descrip TEXT, copyright TEXT, image_url TEXT, source TEXT, published_date TEXT, byline TEXT, added BIT)"

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

    fun addNews(news: NewsObject, tableName: String) {
        val values = ContentValues()
        values.put(url, news.url)
        values.put(title, news.title)
        values.put(descrip, news.descrip)
        values.put(copyright, news.copyright)
        values.put(imageUrl, news.imageUrl)
        values.put(source, news.source)
        values.put(publishedDate, news.publishedDate)
        values.put(byline, news.byline)
        values.put(added, news.added)

        val db = this.writableDatabase

        db.insert(tableName, null, values)
        db.close()
    }

    fun findNews(tableName: String, ID: Int): NewsObject? {
        val query = "SELECT * FROM $tableName WHERE $COLUMN_ID =  \"$ID\""
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        var news: NewsObject? = null

        if (cursor.moveToFirst()) {
            cursor.moveToFirst()
            val _id = Integer.parseInt(cursor.getString(0))
            val url = cursor.getString(1)
            val title = cursor.getString(2)
            val descrip = cursor.getString(3)
            val copyright = cursor.getString(4)
            val imageUrl = cursor.getString(5)
            val source = cursor.getString(6)
            val publishedDate = cursor.getString(7)
            val byline = cursor.getString(8)
            val added = cursor.getInt(9) > 0

            news = NewsObject(url, title, descrip, copyright, imageUrl, source, publishedDate, byline, added)
            cursor.close()
        }

        db.close()
        return news
    }

    fun findNews(tableName: String, newsUrl: String): NewsObject? {
        val query = "SELECT * FROM $tableName WHERE $url =  \"$newsUrl\""
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        var news: NewsObject? = null

        if (cursor.moveToFirst()) {
            cursor.moveToFirst()
            val _id = Integer.parseInt(cursor.getString(0))
            val url = cursor.getString(1)
            val title = cursor.getString(2)
            val descrip = cursor.getString(3)
            val copyright = cursor.getString(4)
            val imageUrl = cursor.getString(5)
            val source = cursor.getString(6)
            val publishedDate = cursor.getString(7)
            val byline = cursor.getString(8)
            val added = cursor.getInt(9) > 0

            news = NewsObject(url, title, descrip, copyright, imageUrl, source, publishedDate, byline, added)
            cursor.close()
        }

        db.close()
        return news
    }

    fun deleteNews(tableName: String, newsTitle: String): Boolean {
        var result = false
        val query = "SELECT * FROM $tableName WHERE $title = \"$newsTitle\""
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
        //db.close()
        return result
    }

    fun setAdded(tableName: String, newsUrl: String): Boolean {
        var result = false
        val query = "UPDATE $tableName SET $added = 1 WHERE $url = \"$newsUrl\""
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        Log.d(TAG, "setAdded: $title")
        cursor.close()
        result = true

        return result
    }

    companion object {
        private const val COLUMN_ID = "_ID"
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "newsData.db"
        const val TABLE_EMAILED = "emailed"
        const val TABLE_VIEWED = "viewed"
        const val TABLE_SHARED = "shared"
        const val TABLE_FAVOURITE = "favourite"
        const val url = "url"
        const val title = "title"
        const val descrip = "descrip"
        const val copyright = "copyright"
        const val imageUrl = "image_url"
        const val source = "source"
        const val publishedDate = "published_date"
        const val byline = "byline"
        const val added = "added"
    }
}
