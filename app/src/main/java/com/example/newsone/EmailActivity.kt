package com.example.newsone

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.example.newsone.DataParse.AsyncResponse
import kotlinx.android.synthetic.main.activity_email.*
import org.json.JSONObject


class EmailActivity : BaseActivity(0), AsyncResponse {
    private val TAG = "EmailActivity"

    private var mDataSet: ArrayList<JSONObject> = ArrayList()
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

    override fun processFinish(output: ArrayList<JSONObject>) {
        mDataSet = output
        data_view.text = output[1].getString("image_url")
    }
}
