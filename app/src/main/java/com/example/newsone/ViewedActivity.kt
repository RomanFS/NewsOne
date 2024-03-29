package com.example.newsone

import android.content.Context
import android.content.res.Configuration
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import kotlinx.android.synthetic.main.activity_email.*
class ViewedActivity : BaseActivity(1), DataParse.AsyncResponse {
    private val TAG = "ViewedActivity"
    private lateinit var task: AsyncTask<Void, Void, Void>
    private lateinit var myDB: MyDBHandler
    private lateinit var myImageDB: ImageDBHandler

    private val parseUrl = "https://api.nytimes.com/svc/mostpopular/v2/viewed/30.json?api-key=jx59ZPEaEg0uKWezOUF4I0KY3ZoAvMiZ"
    private val tableName = "viewed"

    val context: Context = this
    //private val task = DataParse(this, parseUrl, "viewed", context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewed)
        Log.d(TAG, "onCreate: ")

        // setUp BottomNavigation
        setUpBtmNav()

        myDB = getDB()
        myImageDB = getImageDB()

        // start Parser
        task = DataParse(this, parseUrl, tableName, myDB).execute()

        // call adapter
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            news_rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        } else {
            news_rv.layoutManager = LinearLayoutManager(this)
        }
    }

    // Async parser result
    override fun processFinish() {
        Log.d(TAG, "processFinish: start Adapter")
        news_rv.adapter = NewsAdapter(this, tableName, myDB, myImageDB)
    }

    override fun onPause() {
        super.onPause()
        task.cancel(true)
    }
}