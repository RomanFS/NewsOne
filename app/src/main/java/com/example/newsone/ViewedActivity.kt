package com.example.newsone

import android.os.Bundle
import android.util.Log

class ViewedActivity : BaseActivity(1) {
    private val TAG = "ViewedActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewed)
        setUpBtmNav()
        Log.d(TAG, "onCreate: ")
    }
}