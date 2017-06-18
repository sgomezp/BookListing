package com.example.android.booklisting;

/**
 * Created by sgomezp on 14/06/2017.
 */

public class Books {

    // Title of the book
    private String mTitle;

    // Name of the Author
    private String mAuthor;

    /**
     * Constant value that represents no image was provided for this book
     */
    private static final int NO_IMAGE_PROVIDED = -1;

    // Image ID Resource
    private int mImageIdResource = NO_IMAGE_PROVIDED;

    // Book url
    private String mUrl;

    // Thumbnail
    private String mThumbnail;

    /**
     * Create a new Books Object
     *
     * @param title           Title of the book
     * @param author          Name of the author
     * @param url             Url of the book details
     *
     */


    public Books(String title, String author, String thumbnail, String url) {
        // Constructor
        mTitle = title;
        mAuthor = author;
        mThumbnail = thumbnail;
        mUrl = url;

    }


    // Get Title
    public String getTitle() {
        return mTitle;
    }

    // Set Title
    public void setTitle(String title) {
        mTitle = title;
    }

    // Get Author
    public String getAuthor() {
        return mAuthor;
    }

    // Set Author
    public void setAuthor(String author) {
        mAuthor = author;
    }

    // Get Image IdResource
    public int getImageIdResource() {
        return mImageIdResource;
    }

    // Set Image IdResource
    public void setImageIdResource(int imageIdResource) {
        mImageIdResource = imageIdResource;
    }

    // Get the URL
    public String getUrl() {
        return mUrl;
    }

    // Set the URL
    public void setUrl(String url) {
        mUrl = url;
    }

    //Get thumbnail
    public String getThumbnail(){
        return mThumbnail;
    }

    // Set Thumbnail
    public  void setThumbnail(String thumbnail){
        mThumbnail = thumbnail;
    }


    /**
     * Returns whether or not there is an image for this book.
     */
    public boolean hasImage() {

        return mImageIdResource != NO_IMAGE_PROVIDED;
    }

    @Override
    public String toString() {

        return super.toString();
    }
}
