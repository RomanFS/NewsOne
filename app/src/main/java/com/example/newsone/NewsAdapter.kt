package com.example.newsone

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
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

class NewsAdapter(val context: Context,
                  private val tableName: String,
                  private val myDB: MyDBHandler,
                  private val myImageDB: ImageDBHandler)
    : RecyclerView.Adapter<NewsAdapter.ViewHolder>(), ImageParse.AsyncImageResponse {
    private val TAG = "NewsAdapter"
    private lateinit var task: AsyncTask<Void, Void, Void>
    private val map = mutableMapOf<String, Bitmap>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val res = context.resources
        val orientation = res.configuration.orientation
        val view: View
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.news_item_hor, parent, false)
        } else {
            view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        }
        view.image.setImageBitmap(BitmapFactory.decodeResource(res, R.drawable.large))
        view.image.layoutParams.height = res.getDimension(R.dimen.imageHeight).toInt()
        view.image.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
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
        Log.d(TAG, "onBindViewHolder: " + news.added.toString())

        item.see_more.setOnClickListener {
            val intent = Intent(context, InnerActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            val extras = Bundle()
            extras.putString("url", news.url)
            extras.putBoolean("added", news.added)
            extras.putString("title", news.title)
            intent.putExtras(extras)
            startActivity(context, intent, Bundle())
        }

        item.add_to_favourite.setOnClickListener{
            it.isActivated = true
            if (myDB.findNews("favourite", news.url) != null) return@setOnClickListener

            val url = news.url
            val title = news.title
            val descrip =  news.descrip
            val copyright = news.copyright
            val imageUrl = news.imageUrl
            val source = news.source
            val publishedDate = news.publishedDate
            val byline = news.byline
            val added = true

            val addNews = NewsObject(url, title, descrip, copyright, imageUrl, source, publishedDate, byline, added)
            myDB.addNews(addNews, "favourite")

            myDB.setAdded("emailed", news.url)
            Log.d(TAG, "onBindViewHolder: setAdded")
            myDB.setAdded("shared", news.url)
            myDB.setAdded("viewed", news.url)

            if (map[news.imageUrl] == null) return@setOnClickListener
            val imageObj = ImageObject(news.imageUrl, map[news.imageUrl]!!)
            myImageDB.addImage(imageObj, "favourite")
            Toast.makeText(context, "Added to favourite", Toast.LENGTH_LONG).show()
        }

        item.add_to_favourite.isActivated = news.added

        val image = myImageDB.findImage(tableName, news.imageUrl)
        if (image != null) {
            val res = context.resources
            item.image.setImageBitmap(image.bitmap)
            item.image.layoutParams.height = res.getDimension(R.dimen.imageHeight).toInt()
            item.image.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
            map[news.imageUrl] = image.bitmap
            return
        }
        // start ImageParser
        ImageParse(this, news.imageUrl, holder, myImageDB, tableName).execute()
    }

    override fun processFinish(holder: ViewHolder, imageUrl: String) {
        val image = myImageDB.findImage(tableName, imageUrl) ?: return
        val res = context.resources
        map[imageUrl] = image.bitmap
        val imageView = holder.itemView.image
        imageView.setImageBitmap(image.bitmap)
        imageView.layoutParams.height = res.getDimension(R.dimen.imageHeight).toInt()
        imageView.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemCount(): Int {
        return 20
    }
}