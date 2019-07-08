package com.example.newsone

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_inner.*
import android.support.v4.app.NotificationCompat.getExtras



class InnerActivity: AppCompatActivity() {
    private val TAG = "InnerActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inner)

        val extras = intent.extras!!
        val url = extras.getString("url")!!
        val added = extras.getBoolean("added")

        web_view.loadUrl(url)

        added_icon.isActivated = added


        back_arrow.setOnClickListener {
            onBackPressed()
        }
    }
}
