package com.example.newsone

import android.content.Context
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_email.*
import com.example.newsone.DataParse.AsyncResponse


class EmailActivity : BaseActivity(0), AsyncResponse {
    private val TAG = "EmailActivity"

    private val mDataSet: Array<String>? = null
    var context: Context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email)
        Log.d(TAG, "onCreate: ")
        setUpBtmNav()

        DataParse(this).execute()

        //news_rv.layoutManager = LinearLayoutManager(context)
        //news_rv.adapter = NewsAdapter()
    }

    override fun processFinish(output: String) {
        data_view.text = output
    }
}
