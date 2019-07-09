package com.example.newsone

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_inner.*
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import java.io.File
import java.io.File.separator

private val TAG = "InnerActivity"
private val hashDir = "webPages"

class InnerActivity: AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inner)

        back_arrow.setOnClickListener {
            onBackPressed()
        }

        val extras = intent.extras!!
        val title = extras.getString("title")!!
        val url = extras.getString("url")!!
        val added = extras.getBoolean("added")
        added_icon.isActivated = added


        val webSettings = web_view.settings
        webSettings.javaScriptEnabled = true

        if (extras.getString("tableName") == null) {
            web_view.loadUrl(url)
            return
        }

        val hash = DigestUtils.sha1(title+url)
        val hashName = String(Hex.encodeHex(hash)) + ".mht"

        hashPage(hashName, url)
    }

    private fun hashPage(hashName: String, url: String) {
        val client = AndroidWebClient(hashName, filesDir)
        web_view.webViewClient = client
        var file = File(filesDir.absolutePath + separator + hashDir)
        file.mkdirs()


        file = File(file, hashName)
        if (file.exists()) {
            Log.d(TAG, "onCreate: tryLoad $hashName")
            web_view.loadUrl("file:///" + file.path)
        } else {
            Log.d(TAG, "onCreate: fail $hashName")
            web_view.loadUrl(url)
        }
    }
}

private class AndroidWebClient(val hashName: String, val filesDir: File): WebViewClient() {
    override fun onPageStarted(
        view: WebView, url: String,
        favicon: Bitmap?
    ) {}

    override fun onPageFinished(view: WebView, url: String) {
        super.onPageFinished(view, url)
        Log.d(TAG, "onPageFinished: ")
        view.saveWebArchive(
            filesDir.absolutePath + separator + hashDir + separator + hashName
        )
    }

    override fun onLoadResource(view: WebView, url: String) {}
}
