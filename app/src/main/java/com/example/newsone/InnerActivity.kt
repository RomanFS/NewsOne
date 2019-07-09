package com.example.newsone

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_inner.*
import java.io.File
import java.io.File.separator

private val TAG = "InnerActivity"

class InnerActivity: AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inner)
        val extras = intent.extras!!
        val url = extras.getString("url")!!
        val added = extras.getBoolean("added")

        added_icon.isActivated = added

        val webSettings = web_view.settings
        webSettings.javaScriptEnabled = true
        val client = AndroidWebClient()
        web_view.webViewClient = client

        val PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (ContextCompat.checkSelfPermission(this,PERMISSIONS[0])  != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this,PERMISSIONS[1])  != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1)
        }
        val file = File(Environment.getExternalStorageDirectory().absolutePath
                + separator + "myArchive" + ".mht")

        if (file.exists()) {
            Log.d(TAG, "exist")
            web_view.loadUrl(file.absolutePath)
        } else {
            Log.d(TAG, "no exist")
            web_view.loadUrl(url)
        }

        back_arrow.setOnClickListener {
            onBackPressed()
        }
    }
}

private class AndroidWebClient: WebViewClient() {
    override fun onPageStarted(
        view: WebView, url: String,
        favicon: Bitmap?
    ) {
        Log.d(TAG, "onPageStarted: ")
    }

    override fun onPageFinished(view: WebView, url: String) {
        super.onPageFinished(view, url)
        Log.d(TAG, "onPageFinished: ")

        view.saveWebArchive(
            Environment.getExternalStorageDirectory().absolutePath + separator + "myArchive" + ".mht"
        )
        // webarchive will be available now at the above provided location with name "myArchive"+".mht"

    }

    override fun onLoadResource(view: WebView, url: String) {
        Log.d(TAG, "onLoadResource: ")
    }
}
