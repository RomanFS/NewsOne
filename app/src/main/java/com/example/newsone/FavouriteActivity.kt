package com.example.newsone

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import kotlinx.android.synthetic.main.activity_email.*
import kotlinx.android.synthetic.main.activity_favourite.*
import kotlinx.android.synthetic.main.activity_favourite.news_rv

class FavouriteActivity : BaseActivity(3) {
    private val TAG = "FavouriteActivity"
    private lateinit var myDB: MyDBHandler
    private lateinit var myImageDB: ImageDBHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)
        setUpBtmNav()
        Log.d(TAG, "onCreate: ")

        // setUp BottomNavigation
        setUpBtmNav()

        myDB = getDB()
        myImageDB = getImageDB()

        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            news_rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        } else {
            news_rv.layoutManager = LinearLayoutManager(this)
        }
        news_rv.adapter = FavAdapter(this, myDB, myImageDB)
    }
}
