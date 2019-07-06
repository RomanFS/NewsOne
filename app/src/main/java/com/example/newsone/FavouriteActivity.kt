package com.example.newsone

import android.content.Context
import android.os.Bundle
import android.util.Log
import org.json.JSONObject

class FavouriteActivity : BaseActivity(3), DataParse.AsyncResponse {
    private val TAG = "FavouriteActivity"
    val parseUrl = "https://api.nytimes.com/svc/mostpopular/v2/emailed/30.json?api-key=jx59ZPEaEg0uKWezOUF4I0KY3ZoAvMiZ"
    val context: Context = this
    //private val task = DataParse(this, parseUrl).execute()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)
        setUpBtmNav()
        Log.d(TAG, "onCreate: ")

        //DataParse(this, parseUrl).execute()

        //news_rv.layoutManager = LinearLayoutManager(context)
    }

    override fun processFinish(output: ArrayList<JSONObject>) {
        //news_rv.adapter = NewsAdapter(context, output)
    }

    override fun onPause() {
        super.onPause()
        //task.cancel(true)
    }
}
