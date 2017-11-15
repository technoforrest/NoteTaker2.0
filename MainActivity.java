package com.example.schwartz.pa5;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


/**
 * This program implements a note taker to create and store notes
 * This Activity shows and implements the list screen of all of the notes
 * CPSC 312-01, Fall 2017
 * Programming Assignment #7
 *
 * @author Holly Schwartz and Danielle Forrest
 * @version v1.0 11/14/17
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
 *      -_Database- Sellect all, build table,
 *
 *      Danielle Forrest:
 *      - List View (created and implemented list view)
 *      - Buttons (created the Buttons)
 *      - EditTexts (created the EditTexts)
 *      - Spinner (custom spinner adapter)
 *      - Grid Layout (created the grid layout)
 *      - Alert Dialog (created the share button for the alert dialog)
 *      - Database- insert, delete, update
 */

public class MainActivity extends AppCompatActivity {

    //initializations
    private SimpleCursorAdapter cursorAdapter;
    private Cursor cursor;
    private NoteDBHelper databaseHelper;
    static final int REQUEST_CODE = 0;
    private String title;
    private String category;
    private String content;
    private long id;
    private int imageResource;
    private long position;
    private ListView listView;
    private String TAG = "Main";
    /**
     * creates parts of the program
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseHelper = new NoteDBHelper(this);
        Log.d(TAG, "onCreate: ");
        cursor = databaseHelper.getSelectAllNoteCursor();
        cursorAdapter = new SimpleCursorAdapter(
                // 6 arguements to the constructor
                //first three are the same as arrayadapter
                this,
                android.R.layout.activity_list_item,
                cursor,
                // parallel arrays
                // names of columns to get data FROM
                new String[]{NoteDBHelper.TITLE},
                // ids of textviews to show data IN
                new int[]{android.R.id.text1},
                0 // use default behavior
        );
        Cursor newCursor = databaseHelper.getSelectAllNoteCursor();
        cursorAdapter.changeCursor(newCursor);
        //creates list view for all of the notes
        listView = new ListView(this);
        listView.setAdapter(cursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            /**
             * keeps the information of the already created note when going back to edit said note
             * @param adapterView
             * @param view
             * @param i
             * @param l
             */
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Note note = databaseHelper.selectNoteById(l);

                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                intent.putExtra("Id", note.getId());
                intent.putExtra("Title", note.getTitle());
                intent.putExtra("Category", note.getCategory());
                intent.putExtra("Content", note.getContent());
                intent.putExtra("ImageResourceId", note.getImageResource());

                Cursor cursor = databaseHelper.getSelectAllNoteCursor();
                cursorAdapter.changeCursor(cursor);
                listView.setAdapter(cursorAdapter);
                Log.d(TAG, "onItemClick: START ACTIVITY FOR RESULT");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        // set the listview to support multiple selections
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        /**
         * Contextual action menu that allows the user to delete notes
         */
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            /**
             *
             * @param actionMode
             * @param i
             * @param listener
             * @param b
             */
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long listener, boolean b) {
                int numSelected = listView.getCheckedItemCount();
                position = listener;
                actionMode.setTitle(numSelected + " selected");

            }

            /**
             *
             * @param actionMode
             * @param menu
             * @return
             */
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                // inflate cam_menu
                MenuInflater menuInflater = actionMode.getMenuInflater();
                menuInflater.inflate(R.menu.cam_menu, menu);
                return true;
            }

            /**
             *
             * @param actionMode
             * @param menu
             * @return
             */
            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {

                return true;
            }

            /**
             * tells the database to delete the note and redispllays the list
             * @param actionMode
             * @param menuItem
             * @return
             */
            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                int menuId = menuItem.getItemId();
                switch(menuId) {
                    case R.id.deleteMenuAction:
                        databaseHelper.deleteNote(position);
                        Cursor newCursor = databaseHelper.getSelectAllNoteCursor();
                        cursorAdapter.changeCursor(newCursor);
                        return true;
                    default:
                        return false;
                }
            }

            /**
             *
             * @param actionMode
             */
            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        });
        setContentView(listView);
    }

    /**
     * creates the menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // get the menuinflater to inflate our menu
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * creates the button functionality in the menu
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // called when the user clicks on one of our actions
        // add, preferences, about
        // first, figure out which item was clicked
        int menuId = item.getItemId();
        switch(menuId) {
            case R.id.addMenuItem:
                startEditItemActivity();
                return true;
            case R.id.deleteMenuItem:
                deleteEditItemActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /**
     * opens up the food menu activity
     */
    private void startEditItemActivity() {
        // explicit intent
        Intent intent = new Intent(this, NoteActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }
    /**
     * adds a note to the database or updates the database depending on whether or not the note already exists
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "onActivityResult: inside");
            id = data.getLongExtra("Id", -1);
            title = data.getStringExtra("Title");
            category = data.getStringExtra("Category");
            content = data.getStringExtra("Content");
            imageResource = data.getIntExtra("ImageResourceId", 0);
            if(id != -1) {
                Log.d(TAG, "onActivityResult: id != -1");
                databaseHelper.updateNoteById(new Note (id, title, category, content, imageResource));

            }else {
                databaseHelper.insertNote(new Note(id, title, category, content, imageResource));
            }

            Cursor newCursor = databaseHelper.getSelectAllNoteCursor();
            cursorAdapter.changeCursor(newCursor);
        }
    }
    
    /**
     * creates the delete alert dialog when garbage is clicked
     */
    private void deleteEditItemActivity()
    {
        AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
        b.setTitle("Delete All Notes");
        b.setMessage("Are you sure you want to delete all of the notes?");

        b.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int listener) {
                databaseHelper.deleteAll();
                Cursor newCursor = databaseHelper.getSelectAllNoteCursor();
                cursorAdapter.changeCursor(newCursor);

            }
        });

        b.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int listener) {
                dialog.cancel();
            }
        });

        AlertDialog alert;
        alert = b.create();
        alert.show();
    }
}