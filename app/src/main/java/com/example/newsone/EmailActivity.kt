package com.example.newsone

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.example.newsone.DataParse.AsyncResponse
import kotlinx.android.synthetic.main.activity_email.*
import org.json.JSONObject


class EmailActivity : BaseActivity(0), AsyncResponse {
    private val TAG = "EmailActivity"
    private val parseUrl = "https://api.nytimes.com/svc/mostpopular/v2/emailed/30.json?api-key=jx59ZPEaEg0uKWezOUF4I0KY3ZoAvMiZ"
    var context: Context = this
    private val task = DataParse(this, parseUrl).execute()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email)
        Log.d(TAG, "onCreate: ")
        setUpBtmNav()

        news_rv.layoutManager = LinearLayoutManager(context)
    }

    override fun processFinish(output: ArrayList<JSONObject>) {
        news_rv.adapter = NewsAdapter(context, output)
    }

    override fun onPause() {
        super.onPause()
        task.cancel(true)
    }
}
