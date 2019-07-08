package com.example.newsone

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.news_item.view.*

class FavAdapter(val context: Context,
                 private val myDB: MyDBHandler,
                 private val myImageDB: ImageDBHandler): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TAG = "FavAdapter"
    private val tableName = "favourite"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d(TAG, "onCreateViewHolder: $position")
        // set data

        if (myDB.findNews(tableName, position+1) == null) return
        val item = holder.itemView
        val news = myDB.findNews(tableName, position+1)!!

        item.add_to_favourite.visibility = View.GONE
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
            ContextCompat.startActivity(context, intent, Bundle())
        }

        val image = myImageDB.findImage(tableName, position+1) ?: return
        holder.itemView.image.setImageBitmap(image.bitmap)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemCount(): Int {
        return 20
    }

}
