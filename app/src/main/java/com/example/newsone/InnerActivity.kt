package com.example.newsone

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class InnerActivity(val url: String?) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inner)
    }
}
