package com.example.newsone

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.example.newsone.DataParse.AsyncResponse
import kotlinx.android.synthetic.main.activity_email.*
import org.json.JSONObject

private val TAG = "EmailActivity"

class EmailActivity : BaseActivity(0), AsyncResponse {
    private val parseUrl = "https://api.nytimes.com/svc/mostpopular/v2/emailed/30.json?api-key=jx59ZPEaEg0uKWezOUF4I0KY3ZoAvMiZ"
    var context: Context = this
    // start parser
    private val task = DataParse(this, parseUrl, "emailed", context).execute()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email)
        Log.d(TAG, "onCreate: ")

        // setUp BottomNavigation
        setUpBtmNav()

        // create DataBase
        MyDBHandler(this, null, null, 1)

        // set type of RecycleView
        news_rv.layoutManager = LinearLayoutManager(context)
    }

    // Async parser result
    override fun processFinish(output: ArrayList<JSONObject>) {
        // set data to RecycleView
        news_rv.adapter = NewsAdapter(context, output)
    }

    override fun onDestroy() {
        super.onDestroy()
        task.cancel(true)
    }
}
