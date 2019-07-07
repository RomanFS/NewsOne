package com.example.newsone

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import kotlinx.android.synthetic.main.activity_favourite.*
import org.json.JSONObject

class FavouriteActivity : BaseActivity(3) {
    private val TAG = "FavouriteActivity"
    val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)
        setUpBtmNav()
        Log.d(TAG, "onCreate: ")

        //DataParse(this, parseUrl).execute()

        val database = baseContext.openOrCreateDatabase("newsData.db", Context.MODE_PRIVATE, null)
        database.execSQL("DROP TABLE IF EXISTS emailed")

        val query = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null)
        query.use {
            while(it.moveToNext()) {
                Log.d(TAG, "onCreate: " + it.getString(0))
            }
        }

        /*val dataParsed = ArrayList<JSONObject>()
        val query = database.rawQuery("SELECT * FROM emailed", null)
        query.use {
            while (it.moveToNext()) {
                // Cycle through all records
                with(it) {
                    val dataObject = JSONObject()

                    dataObject.put("url", this.getString(1))
                    dataObject.put("title", this.getString(2))
                    dataObject.put("desc", this.getString(3))
                    dataObject.put("copyright", this.getString(4))
                    dataObject.put("image_url", this.getString(5))
                    dataObject.put("source", this.getString(6))
                    dataObject.put("published_date", this.getString(7))
                    dataObject.put("byline", this.getString(8))

                    dataParsed.add(dataObject)
                }
            }
        }*/

        //database.close()

        news_rv.layoutManager = LinearLayoutManager(context)
        //news_rv.adapter = NewsAdapter(context, dataParsed)
    }
}
