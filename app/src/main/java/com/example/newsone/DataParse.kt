package com.example.newsone

import android.os.AsyncTask
import android.util.Log
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class DataParse(
    private val delegate: AsyncResponse,
    private val parseUrl: String,
    private val tableName: String,
    private val myDB: MyDBHandler
) : AsyncTask<Void, Void, Void>() {
    private val TAG = "DataParse"
    var data = ""

    interface AsyncResponse {
        fun processFinish()
    }

    override fun doInBackground(vararg voids: Void): Void? {
        try {
            /*val connectivityManager=EmailActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo=connectivityManager.activeNetworkInfo
            if (!(networkInfo!=null && networkInfo.isConnected)) return null*/

            val url = URL(parseUrl)
            val httpURLConnection = url.openConnection() as HttpURLConnection
            val inputStream = httpURLConnection.inputStream
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            var line: String? = ""
            while (line != null) {
                line = bufferedReader.readLine()
                data += line
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        if (data.isEmpty()) return null
        fetchData()

        Log.d(TAG, "doInBackground: success")
        return null
    }

    private fun fetchData() {
        val jsobj = JSONObject(data)
        if (jsobj.has("fault")) {
            throw Exception("ERROR: timeLimit")
        }
        val jsarray = jsobj.getJSONArray("results")
        Log.d(TAG, "fetchData: ")

        val parseData = jsarray.get(0) as JSONObject

        if (myDB.findNews(tableName, 1) != null) {
            val localData = myDB.findNews(tableName, 1)
            if (parseData.getString("url") == localData!!.url) return
        }

        for (i in 0 until jsarray.length()) {
            val oneNews = jsarray.get(i) as JSONObject

            val url = oneNews.getString("url")
            val title = oneNews.getString("title")
            val descrip = oneNews.getString("abstract")
            val copyright = oneNews.getJSONArray("media").getJSONObject(0).getString("copyright")
            val imageUrl =
                oneNews.getJSONArray("media").getJSONObject(0).getJSONArray("media-metadata").getJSONObject(2)
                    .getString("url")
            val source = oneNews.getString("source")
            val publishedDate = oneNews.getString("published_date")
            val byline = oneNews.getString("byline")
            val added = false

            val news = NewsObject(url, title, descrip, copyright, imageUrl, source, publishedDate, byline, added)

            myDB.addNews(news, tableName)
        }

        /*val query = database.rawQuery("SELECT * FROM $tableName", null)
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
                    //Log.d(TAG, "onCreate: reading data $result")
                }
            }
        }

        database.close()*/
    }

    override fun onPostExecute(result: Void?) {
        delegate.processFinish()
    }
}