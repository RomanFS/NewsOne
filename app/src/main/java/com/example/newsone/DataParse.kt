package com.example.newsone

import android.content.ContentValues
import android.content.Context
import android.os.AsyncTask
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class DataParse(
    private val delegate: AsyncResponse,
    private val parseUrl: String,
    private val tableName: String,
    private val baseContext: Context
) : AsyncTask<Void, Void, ArrayList<JSONObject>>() {
    private val TAG = "DataParse"
    var mContext: Context? = baseContext
    var data = ""
    var dataParsed: ArrayList<JSONObject> = ArrayList(20)

    interface AsyncResponse {
        fun processFinish(output: ArrayList<JSONObject>)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun doInBackground(vararg voids: Void): ArrayList<JSONObject> {
        try {
            val url = URL(parseUrl)
            val httpURLConnection = url.openConnection() as HttpURLConnection
            if (httpURLConnection.responseCode != 200) {
                return ArrayList()
            }
            val inputStream = httpURLConnection.inputStream
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            var line: String? = ""
            while (line != null) {
                //Log.d(TAG, "doInBackground: " + bufferedReader.lines().count())
                //TODO: ЧЕТО СДЕЛАТЬ С ОШИБКОЙ
                line = bufferedReader.readLine()
                data += line
            }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        if (data.isNotEmpty()) {
            fetchData()
            Log.d(TAG, "doInBackground: sacsses")
            return dataParsed
        }
        return ArrayList()
    }

    private fun fetchData() {
        val jsobj = JSONObject(data)
        if (jsobj.has("fault")) {
            //TODO: TOAST
            Log.d(TAG, "error")
            return
        }
        val jsarray = jsobj.getJSONArray("results")
        Log.d(TAG, "fetchData: ")

        //  for (i in jsarray.length()-1 downTo 0) - reversed
        for (i in 0 until jsarray.length()) {
            //Log.d(TAG, "doInBackground: $i")
            val oneNews = jsarray.get(i) as JSONObject
            val dataObject = JSONObject()

            dataObject.put("url", oneNews.getString("url"))
            dataObject.put("title", oneNews.getString("title"))
            dataObject.put("desc", oneNews.getString("abstract"))
            dataObject.put("copyright", oneNews.getJSONArray("media").getJSONObject(0).getString("copyright"))
            dataObject.put(
                "image_url", oneNews.getJSONArray("media").getJSONObject(0)
                    .getJSONArray("media-metadata").getJSONObject(2).getString("url")
            )
            dataObject.put("source", oneNews.getString("source"))
            dataObject.put("published_date", oneNews.getString("published_date"))
            dataObject.put("byline", oneNews.getString("byline"))

            dataParsed.add(dataObject)
            Log.d(TAG, "fetchData: dataParsed")
        }
        //baseContext.deleteDatabase("$tableName.db")
        val database = baseContext.openOrCreateDatabase("newsData.db", Context.MODE_PRIVATE, null)
        database.execSQL("DROP TABLE IF EXISTS $tableName")
        val sql = "CREATE TABLE IF NOT EXISTS $tableName" +
                "(_id INTEGER PRIMARY KEY NOT NULL, url TEXT, title TEXT, " +
                "descrip TEXT, copyright TEXT, image_url TEXT, source TEXT, published_date TEXT, byline TEXT)"
        Log.d(TAG, "onCreate: sql is $sql")
        database.execSQL(sql)


        for (i in 0 until jsarray.length()) {
            val values = ContentValues().apply {
                //Log.d(TAG, "doInBackground: $i")
                val oneNews = jsarray.get(i) as JSONObject
                this.put("url", oneNews.getString("url"))
                this.put("title", oneNews.getString("title"))
                this.put("descrip", oneNews.getString("abstract"))
                this.put("copyright", oneNews.getJSONArray("media").getJSONObject(0).getString("copyright"))
                this.put(
                    "image_url", oneNews.getJSONArray("media").getJSONObject(0)
                        .getJSONArray("media-metadata").getJSONObject(2).getString("url")
                )
                this.put("source", oneNews.getString("source"))
                this.put("published_date", oneNews.getString("published_date"))
                this.put("byline", oneNews.getString("byline"))

                Log.d(TAG, "fetchData: dataSated")
            }
            val generatedId = database.insert(tableName, null, values)
            Log.d(TAG, "onCreate: record added with id $generatedId")
        }

        val query = database.rawQuery("SELECT * FROM $tableName", null)
        query.use {
            while (it.moveToNext()) {
                // Cycle through all records
                with(it) {
                    val id = getLong(0)
                    val url = getString(1)
                    val title = getString(2)
                    val descrip = getString(3)
                    val copyright = getString(4)
                    val imageUrl = getString(5)
                    val source = getString(6)
                    val byline = getString(7)
                    val result =
                        "ID: $id. Name = $url phone = $title email = $descrip copyright = $copyright imageUrl = $imageUrl source = $source byline = $byline"
                    Log.d(TAG, "onCreate: reading data $result")
                }
            }
        }

        database.close()
    }

    override fun onPostExecute(result: ArrayList<JSONObject>) {
        delegate.processFinish(result)
    }
}