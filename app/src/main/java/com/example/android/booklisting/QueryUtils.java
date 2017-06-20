package com.example.android.booklisting;

/**
 * Created by sgomezp on 14/06/2017.
 * <p>
 * Helper methods related to requesting and receiving books data from Google Books API.
 */


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

import static com.example.android.booklisting.MainActivity.LOG_TAG;


public final class QueryUtils {

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */

    private QueryUtils() {
    }

    /**
     * Query the Google Books  dataset and return a list of {@link Books} objects.
     */


    public static List<Books> fetchBookaData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {

        }

        // Extract relevant fields from the JSON response and create a list of {@link Books}
        List<Books> books = extractVolumeInfoFromJson(jsonResponse);

        // Return the list of {@link Books}
        return books;

    }

    /**
     * Returns new URL object from given string URL
     */

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {

        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = null;

        // If the URL is null, then return early
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);

            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());

            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
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
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
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
     * Return a list of {@link Books} objects that has been built up from
     * parsing the given JSON response.
     */

    private static List<Books> extractVolumeInfoFromJson(String bookJSON) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding books to
        List<Books> book = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(bookJSON);

            // if there are no books  in the query return early
            if (baseJsonResponse.getInt("totalItems") == 0) {
                return null;
            }

            // Extract the JSONArray associated with the key called "items",
            // which represents a list of VolumeInfo for books.
            JSONArray bookItemsArray = baseJsonResponse.getJSONArray("items");

            // For each book in the bookItemsArray, create an {@link Books} object
            for (int i = 0; i < bookItemsArray.length(); i++) {
                // Get a single book at position i within the list of books
                JSONObject currentBook = bookItemsArray.getJSONObject(i);

                // For a given book, extract the JSONObject associated with the
                // key called "VolumeInfo", which represents a list of all properties
                // for that book.

                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

                // Extract the value for the key called "title"
                String title = volumeInfo.getString("title");

                String authorName = "";

                if (volumeInfo.has("authors")) {

                    JSONArray authorArray = volumeInfo.getJSONArray("authors");

                    if ((authorArray.length() > 1) && volumeInfo.has("authors")) {

                        for (int j = 0; j < authorArray.length(); j++) {
                            try {
                                authorName = authorName + volumeInfo.getJSONArray("authors").get(j).toString() + ". ";
                            } catch (JSONException e) {
                                Log.i(LOG_TAG, "Problem parsing more than one author");
                            }
                        }

                    } else if ((volumeInfo.has("authors") && authorArray.length() == 1)) {
                        authorName = volumeInfo.getJSONArray("authors").get(0).toString();

                    }
                } else {
                    authorName = "Unknown author";
                }


                // Extract the value for the key called "thumbnail
                String thumbnail = volumeInfo.getJSONObject("imageLinks").getString("thumbnail");


                // Extract the value for the key called "infoLink"
                String infoLink = volumeInfo.getString("infoLink");

                // Create a new {@link Books} object with the title, author from the JSON response.
                Books books_json = new Books(title, authorName, thumbnail, infoLink);

                // Add the new {@link Books} to the list of books.
                book.add(books_json);
                authorName = "";
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the book JSON results", e);


        }
        // return the list of books
        return book;

    }

}