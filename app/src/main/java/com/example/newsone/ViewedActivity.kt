package com.example.newsone

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import kotlinx.android.synthetic.main.activity_email.*
import kotlinx.android.synthetic.main.activity_viewed.*
import kotlinx.android.synthetic.main.activity_viewed.news_rv
import org.json.JSONObject
import kotlin.system.exitProcess

class ViewedActivity : BaseActivity(1), DataParse.AsyncResponse {
    private val TAG = "ViewedActivity"
    val parseUrl = "https://api.nytimes.com/svc/mostpopular/v2/viewed/30.json?api-key=jx59ZPEaEg0uKWezOUF4I0KY3ZoAvMiZ"
    val context: Context = this
    private val task = DataParse(this, parseUrl).execute()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewed)
        setUpBtmNav()
        Log.d(TAG, "onCreate: ")

        DataParse(this, parseUrl).execute()

        news_rv.layoutManager = LinearLayoutManager(context)
    }

    override fun processFinish(output: ArrayList<JSONObject>) {
        news_rv.adapter = NewsAdapter(context, output)
    }

    override fun onPause() {
        super.onPause()
        task.cancel(true)
    }

    override fun onBackPressed() {
        finish()
        exitProcess(0)
    }
}