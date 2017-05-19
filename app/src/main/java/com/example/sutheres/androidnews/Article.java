package com.example.sutheres.androidnews;

/**
 * Created by Sutheres on 1/4/2017.
 */

public class Article {
    // Title of article
    private String mTitle;

    // Section of article
    private String mSection;

    // URL of article
    private String mUrl;


    // Default Constructor
    public Article(String title, String section, String url) {
        mTitle = title;
        mSection = section;
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSection() {
        return mSection;
    }

    public String getURL() {
        return mUrl;
    }

}
