package com.example.newsone

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import kotlinx.android.synthetic.main.activity_email.*

private val TAG = "EmailActivity"

class EmailActivity : BaseActivity(0), DataParse.AsyncResponse {
    private lateinit var task: AsyncTask<Void, Void, Void>
    private lateinit var myDB: MyDBHandler
    private lateinit var myImageDB: ImageDBHandler

    private val parseUrl = "https://api.nytimes.com/svc/mostpopular/v2/emailed/30.json?api-key=jx59ZPEaEg0uKWezOUF4I0KY3ZoAvMiZ"
    private val tableName = "emailed"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email)
        Log.d(TAG, "onCreate: ")

        // setUp BottomNavigation
        setUpBtmNav()

        myDB = getDB()
        myImageDB = getImageDB()

        // start Parser
        task = DataParse(this, parseUrl, tableName, myDB).execute()

        // call adapter
        news_rv.layoutManager = LinearLayoutManager(this)
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
