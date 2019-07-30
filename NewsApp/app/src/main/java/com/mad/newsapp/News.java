package com.mad.newsapp;

import java.io.Serializable;

public class News implements Serializable{
    String author, title, url, urlToImage, publishedAt;

    public News() {
    }

    public News(String author, String title, String url, String urlToImage, String publishedAt) {
        this.author = author;
        this.title = title;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
    }
}
