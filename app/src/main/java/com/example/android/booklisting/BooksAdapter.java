package com.example.android.booklisting;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sgomezp on 14/06/2017.
 */

public class BooksAdapter extends ArrayAdapter<Books> {

    private Context context;

    /**
     * @param context The current context. Used to inflate the layout file.
     * @param book    A list of book objects to display in a list
     */
    public BooksAdapter(Context context, ArrayList<Books> book) {

        super(context, 0, book);
        this.context = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate
                    (R.layout.book_list_item, parent, false);

        }

        // Find the book at the given position in the list of Books
        final Books currentBook = getItem(position);

        // Find the book_image ImageView in book_list_item layout
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.book_image);

        // Using Picasso lib to show the book thumbnail
        String imageUrl = currentBook.getThumbnail();
        Log.i("BooksAdapter TAG", "imageUrl: " + imageUrl);
        Picasso.with(context).load(imageUrl).into(imageView);
        imageView.setVisibility(View.VISIBLE);

        // Find the book_title
        TextView titleView = (TextView) listItemView.findViewById(R.id.book_title);

        // Find the Author Book
        TextView authorView = (TextView) listItemView.findViewById(R.id.book_author);
        titleView.setText(currentBook.getTitle());
        authorView.setText(currentBook.getAuthor());

        // Start the intent for the Book details
        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentBook.getUrl()));
                getContext().startActivity(intent);

            }
        });

        return listItemView;
    }
}
