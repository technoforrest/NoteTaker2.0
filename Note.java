package com.example.schwartz.pa5;

import android.util.Log;

/**
 * This program implements a note taker to create and store notes
 * This class holds all of the note information
 * CPSC 312-01, Fall 2017
 * Programming Assignment #5
 *
 * @author Holly Schwartz and Danielle Forrest
 * @version v1.0 10/19/17
 *
 * contributions:
 *      Holly Schwartz:
 *      - Alert Dialog (created and implemented alert dialog for deleting a note)
 *      - Grid Layout (edited and formatted layout to specifications)
 *      - Grid Layout Features Formatting and Positioning (positioned each part of the gridlayout to specification)
 *      - List View (helped fix errors)
 *      - Buttons (created and formatted the Buttons)
 *      - EditTexts (formatted the EditTexts)
 *      - Spinner (created the spinner)
 *
 *      Danielle Forrest:
 *      - List View (created and implemented list view)
 *      - Buttons (created the Buttons)
 *      - EditTexts (created the EditTexts and saved the user results for these fields)
 *      - Spinner (saved the spinner user results)
 *      - Grid Layout (created the grid layout)
 *      - Alert Dialog (created the share button for the alert dialog)
 */

public class Note  extends Object{

    //initializations
    private long id;
    private String title;
    private String category;
    private String content;
    private int imageResource;
    private String TAG = "Note ";

    /**
     * constructor
     */
    public Note(){
        id = -1;
        title = "BLANK";
        category = "BLANK";
        content ="BLANK";
        imageResource = -1;
    }

    /**
     * Takes the components of a note and adds it to a list
     * @param title String of title of note
     * @param category String of selected spinner category
     * //@param content Edittext containing body of note
     */
    public Note (String title, String category, String content){
        this();
        Log.d(TAG, "Note: inside note constructor");
        this.title = title;
        this.category = category;
        this.content = content;
    }
    public Note (long newId, String title, String category, String content, int newImgResource){
        Log.d(TAG, "Note: inside note constructor");
        this.id = newId;
        this.title = title;
        this.category = category;
        this.content = content;
        this.imageResource = newImgResource;
    }

    /**
     * override function toString
     * @return "title: " + title + ", " + "category: " + category + ", " + "content: " + content
     */
    @Override
    public String toString() {
        return "title: " + title + ", " + "category: " + category + ", " + "content: " + content;
    }

    /**
     * Gets the title of the note
     * @return title of note
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the note
     * @param title title of the note
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the category of the note
     * @return category of note
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category of the note
     * @param category category of the note
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gets the content of the note
     * @return content of note
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content of the note
     * @param content content of the note
     */
    public void setContent(String content) {
        this.content = content;
    }

    public int getImageResource(){
        return imageResource;
    }
    public long getId(){
        return id;
    }
}

