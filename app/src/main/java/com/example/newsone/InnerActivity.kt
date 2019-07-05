package com.example.newsone

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_inner.*

class InnerActivity: AppCompatActivity() {
    private val TAG = "InnerActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inner)

        val url = intent.getStringExtra("url")

        web_view.loadUrl(url)
    }
}
