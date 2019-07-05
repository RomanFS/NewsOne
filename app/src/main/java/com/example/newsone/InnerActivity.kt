package com.example.newsone

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class InnerActivity: AppCompatActivity() {
    private val TAG = "InnerActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inner)

        Log.d(TAG, intent.getStringExtra("url"))
    }
}
