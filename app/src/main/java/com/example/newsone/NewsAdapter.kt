package com.example.newsone

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.news_item.view.*
import org.json.JSONObject

class NewsAdapter(val context: Context,
                  private val tableName: String,
                  private val myDB: MyDBHandler,
                  private val myImageDB: ImageDBHandler)
    : RecyclerView.Adapter<NewsAdapter.ViewHolder>(), ImageParse.AsyncImageResponse {
    private val TAG = "NewsAdapter"
    private lateinit var task: AsyncTask<Void, Void, Void>
    private val map = mutableMapOf<Int, Bitmap>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onCreateViewHolder: $position")
        // set data

        if (myDB.findNews(tableName, position+1) == null) return
        val item = holder.itemView
        val news = myDB.findNews(tableName, position+1)!!
        item.title.text = news.title
        item.desc.text = news.descrip
        item.copyright.text = news.copyright
        item.source.text = news.source
        item.published_date.text = news.publishedDate
        item.byline.text = news.byline
        item.see_more.setOnClickListener {
            val intent = Intent(context, InnerActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            intent.putExtra("url", news.url)
            startActivity(context, intent, Bundle())
        }

        item.add_to_favourite.setOnClickListener{
            if (myDB.findNews("favourite", news.url) != null) return@setOnClickListener

            val url = news.url
            val title = news.title
            val descrip =  news.descrip
            val copyright = news.copyright
            val imageUrl = news.imageUrl
            val source = news.source
            val publishedDate = news.publishedDate
            val byline = news.byline

            val addNews = NewsObject(url, title, descrip, copyright, imageUrl, source, publishedDate, byline)
            myDB.addNews(addNews, "favourite")

            val imageObj = ImageObject(news.imageUrl, map[position+1]!!)
            myImageDB.addImage(imageObj, "favourite")
            Toast.makeText(context, "Added to favourite", Toast.LENGTH_LONG).show()
        }

        // start ImageParser
        ImageParse(this, news.imageUrl, holder, myImageDB, tableName, position+1).execute()
    }

    override fun processFinish(holder: ViewHolder, position: Int) {
        val image = myImageDB.findImage(tableName, position) ?: return
        map[position] = image.bitmap
        holder.itemView.image.setImageBitmap(image.bitmap)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemCount(): Int {
        return 20
    }
}