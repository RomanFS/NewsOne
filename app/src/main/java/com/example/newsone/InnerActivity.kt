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
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.codec.digest.MessageDigestAlgorithms.*
import java.io.File
import java.io.File.separator

private val TAG = "InnerActivity"

class InnerActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inner)

        back_arrow.setOnClickListener {
            onBackPressed()
        }

        val extras = intent.extras!!


        val added = extras.getBoolean("added")
        added_icon.isActivated = added

        if (extras.getString("tableName") == null) return

        val title = extras.getString("title")!!
        val url = extras.getString("url")!!
        val hashName = DigestUtils.sha1Hex(title+url) + ".mht"

        hashPage(hashName, url)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun hashPage(hashName: String, url: String) {
        val webSettings = web_view.settings
        webSettings.javaScriptEnabled = true
        val client = AndroidWebClient(hashName)
        web_view.webViewClient = client

        val PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (ContextCompat.checkSelfPermission(this, PERMISSIONS[0]) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, PERMISSIONS[1]) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1)
        }
        var file = File(Environment.getExternalStorageDirectory().absolutePath + separator + "webPages")
        file.mkdirs()


        file = File(file, hashName)
        if (file.exists()) {
            Log.d(TAG, "onCreate: tryLoad $hashName")
            web_view.loadUrl("file:///" + file.absolutePath)
        } else {
            Log.d(TAG, "onCreate: fail $hashName")
            web_view.loadUrl(url)
        }
    }
}

private class AndroidWebClient(val hashName: String): WebViewClient() {
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
            Environment.getExternalStorageDirectory().absolutePath + separator + "webPages" + separator + hashName
        )
        // webarchive will be available now at the above provided location with name "myArchive"+".mht"

    }

    override fun onLoadResource(view: WebView, url: String) {
        Log.d(TAG, "onLoadResource: ")
    }
}
