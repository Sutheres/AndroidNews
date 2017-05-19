package com.example.sutheres.androidnews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int ARTICLE_LOADER_ID = 1;
    private ArticleAdapter mAdapter;
    private static final String API_KEY = "";
    /**
     * URL to Query Guardian news API
     **/
    private static final String ARTICLES_REQUEST_URL = "https://content.guardianapis.com/search?api-key=" + API_KEY + "&q=android&tag=technology/android&order-by=newest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView articleListView = (ListView) findViewById(R.id.list);

        articleListView.setEmptyView(findViewById(R.id.empty));

        // create a new adapter that takes an empty list of articles as input
        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());

        /**
         * set the adapter on the {@link ListView}
         * so the list can be populated in the user interface.
         */
        articleListView.setAdapter(mAdapter);


        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current Article that was clicked on
                Article currentArticle = mAdapter.getItem(position);

                // Convert the string URL into a URI object (to pass into the Intent Constructor)
                Uri articleURI = Uri.parse(currentArticle.getURL());

                // Create a new Intent to view the earthquake Uri
                Intent intent = new Intent(Intent.ACTION_VIEW, articleURI);

                // send the intent to launch a new activity
                startActivity(intent);
            }
        });


        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            getLoaderManager().initLoader(ARTICLE_LOADER_ID, null, this).forceLoad();
        } else {
            ProgressBar loading = (ProgressBar) findViewById(R.id.loading_spinner);
            loading.setVisibility(View.GONE);
            TextView emptyView = (TextView) findViewById(R.id.empty);
            emptyView.setText("No Internet Connection");
        }

    }


    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle args) {

        mAdapter.clear();

        return new ArticleLoader(this, ARTICLES_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        // clear the adapter of previous earthquake data
        mAdapter.clear();

        TextView emptyView = (TextView) findViewById(R.id.empty);
        emptyView.setText("No articles found");

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
        progressBar.setVisibility(View.GONE);


        // If there is a valid list of {@link Article}s, then add them to the
        // adapters data set. This will trigger the ListView to update
        if (articles != null && !articles.isEmpty()) {
            mAdapter.addAll(articles);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        mAdapter.clear();
    }


}
