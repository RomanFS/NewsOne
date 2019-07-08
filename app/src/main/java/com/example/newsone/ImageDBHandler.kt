package com.example.newsone

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import java.io.ByteArrayOutputStream


class ImageDBHandler(context: Context, name: String?,
                     factory: SQLiteDatabase.CursorFactory?, version: Int) :
    SQLiteOpenHelper(context, DATABASE_NAME,
        factory, DATABASE_VERSION) {
    private val TAG = "ImageDBHandler"

    override fun onCreate(db: SQLiteDatabase) {
        val tableData = "(_id INTEGER PRIMARY KEY NOT NULL, url TEXT, bitmap BLOP)"

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

    fun addImage(image: ImageObject, tableName: String) {
        val values = ContentValues()
        values.put(url, image.url)

        val blob = ByteArrayOutputStream()
        image.bitmap.compress(CompressFormat.PNG, 0 /* Ignored for PNGs */, blob)
        val bitmapdata = blob.toByteArray()

        values.put(bitmap, bitmapdata)

        val db = this.writableDatabase

        db.insert(tableName, null, values)
        //db.close()
    }

    fun findImage(tableName: String, ID: Int): ImageObject? {
        val query = "SELECT * FROM $tableName WHERE _ID =  \"$ID\""
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        var image: ImageObject? = null

        if (cursor.moveToFirst()) {
            cursor.moveToFirst()
            val _id = Integer.parseInt(cursor.getString(0))
            val url = cursor.getString(1)
            val bitmap = BitmapFactory.decodeByteArray(cursor
                .getBlob(2), 0, cursor.getBlob(2).size)

            image = ImageObject(url, bitmap)
            cursor.close()
        }

        //db.close()
        return image
    }

    fun deleteImage(tableName: String,newsTitle: String): Boolean {
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
        //db.close()
        return result
    }

    companion object {
        private const val COLUMN_ID = "_ID"
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "newsImageData.db"
        const val TABLE_EMAILED = "emailed"
        const val TABLE_VIEWED = "viewed"
        const val TABLE_SHARED = "shared"
        const val TABLE_FAVOURITE = "favourite"
        const val url = "url"
        const val bitmap = "bitmap"
    }
}