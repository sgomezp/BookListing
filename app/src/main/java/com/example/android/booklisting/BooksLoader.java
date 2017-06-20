package com.example.android.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by sgomezp on 14/06/2017.
 */

public class BooksLoader extends AsyncTaskLoader<List<Books>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = BooksLoader.class.getName();

    /**
     * Query URL
     */
    private String mUrl;


    /**
     * Construct a new {@link BooksLoader}.
     *
     * @param context of the activity
     * @param url     to load data from
     */

    public BooksLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {

        forceLoad();
    }

    /**
     * This is on a background thread.
     */

    @Override
    public List<Books> loadInBackground() {

        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of books.
        List<Books> books = QueryUtils.fetchBookaData(mUrl);

        return books;
    }
}
