package com.example.newsone

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log

class ImageParse(private val delegate: AsyncImageResponse,
                 val url: String,
                 val holder: NewsAdapter.ViewHolder) : AsyncTask<Void, Void, Bitmap?>() {
    private val TAG = "ImageParse"
    private var mIcon: Bitmap? = null

    interface AsyncImageResponse {
        fun processFinish(output: Bitmap?, holder: NewsAdapter.ViewHolder)
    }

    override fun doInBackground(vararg params: Void): Bitmap? {
        try {
            val stream = java.net.URL(url).openStream()
            mIcon = BitmapFactory.decodeStream(stream)
        } catch (e: Exception) {
            Log.e("Error", e.message)
            e.printStackTrace()
        }

        return mIcon
    }

    override fun onPostExecute(result: Bitmap?) {
        delegate.processFinish(result, holder)
    }
}
