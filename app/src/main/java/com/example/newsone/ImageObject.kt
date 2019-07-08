package com.example.newsone

import android.graphics.Bitmap

class ImageObject {
    var id: Int = 0
    var url = ""
    var w = 440
    var h = 293
    var conf: Bitmap.Config = Bitmap.Config.ARGB_8888 // see other conf types
    var bitmap = Bitmap.createBitmap(w, h, conf)

    constructor(id: Int, url: String, bitmap: Bitmap) {
        this.id = id
        this.url = url
        this.bitmap = bitmap
    }

    constructor(url: String, bitmap: Bitmap) {
        this.url = url
        this.bitmap = bitmap
    }
}