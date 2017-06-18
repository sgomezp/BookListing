package com.example.android.booklisting;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Books>> {

    //LOG_TAG for debugging purposes
    public static final String LOG_TAG = MainActivity.class.getName();
    /**
     * Constant value for the book loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int BOOK_LOADER_ID = 1;
    // URL for Google Books API data
    private static final String BASE_BOOKS_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=";
    // Max Result for  the query
    private static final String MAX_RESULTS_QUERY = "&maxResults=10";
    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;
    //Adapter for the list of books
    private BooksAdapter mAdapter;
    // Search String variable
    private String userQuery = "";
    // Final URL
    private String finalQueryUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Prevent the soft keyboard from pushing the view up
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Find a reference to the {@link ListView} in the layout
        ListView bookListView = (ListView) findViewById(R.id.list);

        //Set the EmptyView
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        mEmptyStateTextView.setVisibility(View.VISIBLE);

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(BOOK_LOADER_ID, null, MainActivity.this);



        // Create a new adapter that takes the list of books as input
        mAdapter = new BooksAdapter(this, new ArrayList<Books>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        bookListView.setAdapter((mAdapter));

        View searchIcon = findViewById(R.id.search_icon);
        searchIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Hide the soft keyboard when  Search button is clicked
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null :
                        getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                EditText editText = (EditText) findViewById(R.id.search);
                userQuery = editText.getText().toString().replace(" ","+");
                finalQueryUrl = BASE_BOOKS_REQUEST_URL + userQuery + MAX_RESULTS_QUERY;


                // Get a reference to the ConnectivityManager to check state of network connectivity
                ConnectivityManager cm = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);

                // Get details on the currently active default data network
                final NetworkInfo networkInfo = cm.getActiveNetworkInfo();

                // If there is a network connection, fetch data
                if (networkInfo != null && networkInfo.isConnected()) {
                    // Get a reference to the LoaderManager, in order to interact with loaders.
                    LoaderManager loaderManager = getLoaderManager();

                    //Hide the initial empty screen message so the loading indicator will be
                    // more visible
                    mEmptyStateTextView.setVisibility(View.GONE);

                    // Initialize the loader. Pass in the int ID constant defined above and pass in null for
                    // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
                    // because this activity implements the LoaderCallbacks interface).

                    loaderManager.restartLoader(BOOK_LOADER_ID, null, MainActivity.this);



                } else {
                    // Otherwise, display error
                    // First, hide loading indicator so error message will be visible
                    View loadingIndicator = findViewById(R.id.loading_indicator);
                    loadingIndicator.setVisibility(View.GONE);

                    // Update empty state with no connection error message
                    mEmptyStateTextView.setText(R.string.no_internet_connection);
                }

            }
        });


    }

    @Override
    public Loader<List<Books>> onCreateLoader(int id, Bundle args) {
        // Create a new loader for the given URL
        // Show loading indicator because the data hasn't been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.VISIBLE);

        return new BooksLoader(this, finalQueryUrl);

    }

    @Override
    public void onLoadFinished(Loader<List<Books>> loader, List<Books> book) {

        //  give the focus to the main_layout
        findViewById(R.id.main_layout).requestFocus();

        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Clear the adapter of previous book data
        mAdapter.clear();

        // If there is a valid list of {@link Books}, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (book != null && !book.isEmpty()) {
            mAdapter.addAll(book);
        } else {

            // Set empty state text to display "No books found."

            mEmptyStateTextView.setText(R.string.no_books);

        }


    }

    @Override
    public void onLoaderReset(Loader<List<Books>> loader) {
        // Loader reset, so we can clear out our existing data.

        mAdapter.clear();

    }
}
