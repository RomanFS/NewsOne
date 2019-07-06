package com.example.newsone

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import kotlinx.android.synthetic.main.activity_viewed.*
import org.json.JSONObject
import kotlin.system.exitProcess

class ViewedActivity : BaseActivity(1), DataParse.AsyncResponse {
    private val TAG = "ViewedActivity"
    private val parseUrl = "https://api.nytimes.com/svc/mostpopular/v2/viewed/30.json?api-key=jx59ZPEaEg0uKWezOUF4I0KY3ZoAvMiZ"
    val context: Context = this
    private val task = DataParse(this, parseUrl, "viewed", context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewed)
        setUpBtmNav()
        Log.d(TAG, "onCreate: ")

        task.execute()

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