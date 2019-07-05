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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)

        // set data
        view.title.text = "The Business of Health Care Depends on Exploiting Doctors and Nurses"
        //view.image.source.text = ""
        view.desc.text = "One resource seems infinite and free: the professionalism of caregivers."
        view.copyright.text = "Delcan &amp; Company"
        view.source.text = "The New York Times"
        view.published_date.text = "2019-06-08"
        view.byline.text = "By DANIELLE OFRI"

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