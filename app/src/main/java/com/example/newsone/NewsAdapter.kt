package com.example.newsone

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.news_item.view.*

class NewsAdapter(val context: Context, val bundle: Bundle?): RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    private val mDataSet: Array<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        view.see_more.setOnClickListener {
            val intent = Intent(context, InnerActivity::class.java)
            intent.putExtra("url", "haha")
            startActivity(context, intent, bundle)
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemCount(): Int {
        return 8
        //return mDataSet.length;
    }
}