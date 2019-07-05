package com.example.newsone

import android.os.AsyncTask
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class DataParse(val delegate: AsyncResponse) : AsyncTask<Void, Void, String?>() {
    var data = ""
    var dataParsed = ""
    var singleParsed = ""

    interface AsyncResponse {
        fun processFinish(output: String)
    }


    override fun doInBackground(vararg voids: Void): String? {
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

            val JA = JSONArray(data)
            for (i in 0 until JA.length()) {
                val JO = JA.get(i) as JSONObject
                singleParsed = "status: " + JO.get("status")
                    /*"Name:" + JO.get("name") + "\n" +
                        "Password:" + JO.get("password") + "\n" +
                        "Contact:" + JO.get("contact") + "\n" +
                        "Country:" + JO.get("country") + "\n"*/

                dataParsed = dataParsed + singleParsed + "\n"
            }

        } catch (e: Throwable) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return null
    }

    override fun onPostExecute(result: String?) {
        delegate.processFinish(data)
    }
}