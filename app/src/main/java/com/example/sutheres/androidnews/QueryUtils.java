package com.example.sutheres.androidnews;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sutheres on 1/4/2017.
 */

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {

    }

    public static List<Article> extractArticles(String stringURL) {
        // Create URL object
        URL url = createURL(stringURL);

        // Perform HTTP Request to the URL and receive a JSON response back
        String jsonResponse = "";
        try {
            jsonResponse = makeHTTPRequest(url);
        } catch (IOException e) {

        }


        // Extract relevant fields from JSON response and create an ArrayList of {@link Article}s
        List<Article> articles = extractFeaturesFromJSON(jsonResponse);

        return articles;
    }

    /**
     * Returns new URL object from the given URL
     */
    private static URL createURL(String stringURL) {

        Uri baseUri = Uri.parse(stringURL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("maxResults", "14");

        URL url = null;
        try {
            url = new URL(uriBuilder.toString());
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL", e);
            return null;
        }

        return url;

    }

    /**
     * Make an HTTP Request with the given URL and receive a string response
     */
    private static String makeHTTPRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If URL is null return an empty JSON response
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error: " + urlConnection.getResponseCode());
            }
        } catch (IOException exception) {
            Log.e(LOG_TAG, "Problem receiving JSON result", exception);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a string which contains the whole
     * JSON response from the server
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return an Arraylist of {@link Article}s by parsing out information
     * about the articles from the input articleJSON string.
     */
    private static List<Article> extractFeaturesFromJSON(String articleJSON) {
        if (TextUtils.isEmpty(articleJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding articles to
        List<Article> articles = new ArrayList<>();

        // Try to parse the articles API JSON response. if there's a problem with the way the json
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Parse the response given by the articles API response string and
            // build up a list of article objects with the corresponding data
            JSONObject rootJSONResponse = new JSONObject(articleJSON);

            /**************** new code from today **************/
            JSONObject responseJSONObject = rootJSONResponse.getJSONObject("response");

            // Get the instance of JSONArray that contains the articles (items)
            JSONArray resultsArray = responseJSONObject.getJSONArray("results");

            // Loop through each article in the array
            for (int i = 0; i < resultsArray.length(); i++) {
                // access current article
                JSONObject rootJSONObject = resultsArray.getJSONObject(i);

                String title = rootJSONObject.getString("webTitle");
                String section = rootJSONObject.getString("sectionName");
                String url = rootJSONObject.getString("webUrl");

                // Create Article object from author and title above and add it to the list of Articles
                Article article = new Article(title, section, url);
                articles.add(article);
            }


        } catch (JSONException exception) {

            // If an error is thrown when executing any of the above statements in the try block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing JSON results: ", exception);
        }

        // return list of articles
        return articles;
    }

}
