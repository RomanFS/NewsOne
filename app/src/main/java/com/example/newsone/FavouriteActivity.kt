package com.example.newsone

import android.os.Bundle
import android.util.Log

class FavouriteActivity : BaseActivity(3) {
    private val TAG = "FavouriteActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)
        setUpBtmNav()
        Log.d(TAG, "onCreate: ")
    }
}
