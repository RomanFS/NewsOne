package com.example.newsone

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
                  private val mDataSet: ArrayList<JSONObject> = ArrayList())
    : RecyclerView.Adapter<NewsAdapter.ViewHolder>(), ImageParse.AsyncImageResponse {

    private val TAG = "NewsAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onCreateViewHolder: $position")
        // set data
        val item = holder.itemView
        item.title.text = mDataSet[position].getString("title")
        item.desc.text = mDataSet[position].getString("desc")
        item.copyright.text = mDataSet[position].getString("copyright")
        item.source.text = mDataSet[position].getString("source")
        item.published_date.text = mDataSet[position].getString("published_date")
        item.byline.text = mDataSet[position].getString("byline")

        item.see_more.setOnClickListener {
            val intent = Intent(context, InnerActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            intent.putExtra("url", mDataSet[position].getString("url"))
            startActivity(context, intent, Bundle())
        }
        item.add_to_favourite.setOnClickListener{
            Toast.makeText(context, "Added to favourite", Toast.LENGTH_LONG).show()
        }

        // default image
        val res = context.resources
        item.image.setImageBitmap(BitmapFactory.decodeResource(res, R.drawable.large))

        // get Image Bitmap
        ImageParse(this, mDataSet[position].getString("image_url"), holder).execute()
    }

    override fun processFinish(output: Bitmap?, holder: ViewHolder) {
        holder.itemView.image.setImageBitmap(output)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemCount(): Int {
        return mDataSet.size
    }

}