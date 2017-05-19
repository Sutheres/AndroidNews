package com.example.sutheres.androidnews;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Sutheres on 1/4/2017.
 */

public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    private String mUrl;
    List<Article> mArticles;

    public ArticleLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Article> loadInBackground() {
        // DOnt perform the request if there are no URLs, or the first URL is null
        if (mUrl == null) {
            return null;
        }

        // Perform the HTTP request for earthquake data and receive a response
        mArticles = QueryUtils.extractArticles(mUrl);
        return mArticles;
    }


}
