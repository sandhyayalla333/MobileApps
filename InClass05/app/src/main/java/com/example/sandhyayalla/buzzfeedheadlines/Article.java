package com.example.sandhyayalla.buzzfeedheadlines;

public class Article {
    String title,publishedAt,urlToImage,description;
    public Article()
    {}

    @Override
    public String toString() {
        return "Article{" +
                "title='" + title + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}

//title, publishedAt, urlToImage, and description.
