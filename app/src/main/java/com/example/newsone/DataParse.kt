package com.example.newsone

import android.os.AsyncTask
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class DataParse(val delegate: AsyncResponse) : AsyncTask<Void, Void, ArrayList<JSONObject>>() {
    private val TAG = "DataParse"
    var data = ""
    var dataParsed: ArrayList<JSONObject> = ArrayList(20)

    interface AsyncResponse {
        fun processFinish(output: ArrayList<JSONObject>)
    }


    override fun doInBackground(vararg voids: Void): ArrayList<JSONObject> {
        try {
            val url = URL("https://api.nytimes.com/svc/mostpopular/v2/emailed/30.json?api-key=jx59ZPEaEg0uKWezOUF4I0KY3ZoAvMiZ")
            val httpURLConnection = url.openConnection() as HttpURLConnection
            val inputStream = httpURLConnection.inputStream
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            var line: String? = ""
            while (line != null) {
                line = bufferedReader.readLine()
                data += line
            }

            val jsobj = JSONObject(data)
            val jsarray = jsobj.getJSONArray("results")
            for (i in 0 until  jsarray.length()) {
                val oneNews = jsarray.get(i) as JSONObject
                val dataObject = JSONObject()

                dataObject.put("url", oneNews.getString("url"))
                dataObject.put("title", oneNews.getString("title"))
                dataObject.put("desc", oneNews.getString("abstract"))
                dataObject.put("copyright", oneNews.getJSONArray("media").getJSONObject(0).getString("copyright"))
                dataObject.put("copyright", oneNews.getJSONArray("media").getJSONObject(0)
                    .getJSONArray("media-metadata").getJSONObject(2).getString("url"))
                dataObject.put("source", oneNews.getString("source"))
                dataObject.put("published_date", oneNews.getString("published_date"))
                dataObject.put("byline", oneNews.getString("byline"))

                dataParsed.add(oneNews)
            }

        } catch (e: Throwable) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        Log.d(TAG, "doInBackground: " + dataParsed.size)
        return dataParsed
    }

    override fun onPostExecute(result: ArrayList<JSONObject>) {
        delegate.processFinish(result)
    }
}