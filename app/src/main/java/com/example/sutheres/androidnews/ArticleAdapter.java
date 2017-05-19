package com.example.sutheres.androidnews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sutheres on 1/4/2017.
 */

public class ArticleAdapter extends ArrayAdapter<Article> {

    public ArticleAdapter(Context context, ArrayList<Article> articles) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a ListView.
        // Because this is a custom adapter, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, articles);
    }


    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The position in the list of data that should be displayed in the
     *                    list item view.
     * @param convertView The recycled view to populate.
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);

        }

        // Get the {@link Article} object located at this position in the list
        Article currentArticle = getItem(position);

        // Find the TextView in the list_item.xml file with the ID title
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title);

        // Get the current Title from the Article object and set it on this TextView
        titleTextView.setText(currentArticle.getTitle());

        // Find the TextView in the list_item.xml file with the ID author
        TextView sectionTextView = (TextView) listItemView.findViewById(R.id.section);

        // Get the current author from the Article object and set it on this TextView
        sectionTextView.setText(currentArticle.getSection());


        // Return the whole list item layout (Containing 2 TextViews)
        // so that it can be shown in the ListView
        return listItemView;
    }

}
