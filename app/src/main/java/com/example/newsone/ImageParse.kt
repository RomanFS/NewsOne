package com.example.newsone

import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log

class ImageParse(private val delegate: AsyncImageResponse,
                 private val url: String,
                 private val holder: NewsAdapter.ViewHolder,
                 private val myDb: ImageDBHandler,
                 private val tableName: String,
                 private val position: Int) : AsyncTask<Void, Void, Void>() {
    private val TAG = "ImageParse"

    interface AsyncImageResponse {
        fun processFinish(holder: NewsAdapter.ViewHolder, position: Int)
    }

    override fun doInBackground(vararg params: Void): Void? {
        try {
            val stream = java.net.URL(url).openStream()
            val mIcon = BitmapFactory.decodeStream(stream) ?: return null

            myDb.addImage(ImageObject(url, mIcon), tableName)
        } catch (e: Exception) {
            Log.e("Error", e.message)
            e.printStackTrace()
        }

        return null
    }

    override fun onPostExecute(result: Void?) {
        delegate.processFinish(holder, this.position)
    }
}
