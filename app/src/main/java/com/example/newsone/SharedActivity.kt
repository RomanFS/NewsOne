package com.example.newsone

import android.os.Bundle
import android.util.Log

class SharedActivity : BaseActivity(2) {
    private val TAG = "SharedActivity"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shared)
        setUpBtmNav()
        Log.d(TAG, "onCreate: ")
    }
}
