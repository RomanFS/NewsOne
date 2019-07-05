package com.example.newsone

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
import kotlin.math.log


class DataParse(private val delegate: AsyncResponse, private val parseUrl: String) : AsyncTask<Void, Void, ArrayList<JSONObject>>() {
    private val TAG = "DataParse"
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
        for (i in jsarray.length()-1 downTo 0) {
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
    }

    override fun onPostExecute(result: ArrayList<JSONObject>) {
        delegate.processFinish(result)
    }
}