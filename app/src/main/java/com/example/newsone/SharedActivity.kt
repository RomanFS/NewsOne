package com.example.newsone

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import kotlinx.android.synthetic.main.activity_shared.*
import org.json.JSONObject

class SharedActivity : BaseActivity(2), DataParse.AsyncResponse {
    private val TAG = "SharedActivity"
    private val parseUrl = "https://api.nytimes.com/svc/mostpopular/v2/shared/30.json?api-key=jx59ZPEaEg0uKWezOUF4I0KY3ZoAvMiZ"
    val context: Context = this
    private val task = DataParse(this, parseUrl, "shared", context)
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shared)
        setUpBtmNav()
        Log.d(TAG, "onCreate: ")

        task.execute()

        news_rv.layoutManager = LinearLayoutManager(context)
    }

    override fun processFinish(output: ArrayList<JSONObject>) {
        news_rv.adapter = NewsAdapter(context, output)
    }
}
