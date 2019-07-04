package com.example.newsone

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import kotlinx.android.synthetic.main.activity_favourite.*

class FavouriteActivity : BaseActivity(3) {
    private val TAG = "FavouriteActivity"
    val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)
        setUpBtmNav()
        Log.d(TAG, "onCreate: ")

        news_rv.layoutManager = LinearLayoutManager(context)
        news_rv.adapter = NewsAdapter()
    }
}
