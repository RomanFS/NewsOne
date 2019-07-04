package com.example.newsone

import android.os.Bundle
import android.util.Log

class EmailActivity : BaseActivity(0) {
    private val TAG = "EmailActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email)
        setUpBtmNav()
        Log.d(TAG, "onCreate: ")
    }
}
