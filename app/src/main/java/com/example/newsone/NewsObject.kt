package com.example.newsone

class NewsObject {
    var id: Int = 0
    var url = ""
    var title = ""
    var descrip = ""
    var copyright = ""
    var imageUrl = ""
    var source = ""
    var publishedDate = ""
    var byline = ""
    var added = false

    constructor(id: Int, url: String, title: String, descrip: String, copyright: String, image_url: String, source: String,
                published_date: String, byline: String, added: Boolean) {
        this.id = id
        this.url = url
        this.title = title
        this.descrip = descrip
        this.copyright = copyright
        this.imageUrl = image_url
        this.source = source
        this.publishedDate = published_date
        this.byline = byline
        this.added = added
    }

    constructor(url: String, title: String, descrip: String, copyright: String, image_url: String, source: String,
                published_date: String, byline: String, added: Boolean) {
        this.url = url
        this.title = title
        this.descrip = descrip
        this.copyright = copyright
        this.imageUrl = image_url
        this.source = source
        this.publishedDate = published_date
        this.byline = byline
        this.added = added
    }
}