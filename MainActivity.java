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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


import java.util.ArrayList;
import java.util.List;

/**
 * This program implements a note taker to create and store notes
 * This Activity shows and implements the list screen of all of the notes
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
 *      - EditTexts (created the EditTexts)
 *      - Spinner (saved the spinner user results)
 *      - Grid Layout (created the grid layout)
 *      - Alert Dialog (created the share button for the alert dialog)
 */

public class MainActivity extends AppCompatActivity {

    //initializations
    private SimpleCursorAdapter cursorAdapter;
    private Cursor cursor;
    private NoteDBHelper databaseHelper;
    private List<String> titleList;
    private ArrayList<Note> noteList = new ArrayList<>();
    private ArrayAdapter titleArrayAdapter;
    static final int REQUEST_CODE = 0;
    private ListView titleListView;
    private String title;
    private String category;
    private String content;
    private int id;
    private int imageResource;
    private long position;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

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
                startActivityForResult(intent, REQUEST_CODE);


            }
        });
        Cursor newCursor1 = databaseHelper.getSelectAllNoteCursor();
        cursorAdapter.changeCursor(newCursor1);
        final ListView listView = new ListView(this);
        listView.setAdapter(cursorAdapter);
        // set the listview to support multiple selections
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long listener, boolean b) {
                int numSelected = listView.getCheckedItemCount();
                position = listener;
                actionMode.setTitle(numSelected + " selected");

            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                // inflate cam_menu
                MenuInflater menuInflater = actionMode.getMenuInflater();
                menuInflater.inflate(R.menu.cam_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {

                return true;
            }

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
//cam mode delete- not alert dialog
//switch statement to check id of what was selected
//adapter
            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        });

        Cursor newCursor2 = databaseHelper.getSelectAllNoteCursor();
        cursorAdapter.changeCursor(newCursor2);

      /*  listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {


            /**
             * creates and implements Alert Dialog for deleting a note
             * @param adapterView
             * @param view
             * @param i
             * @param listener
             * @return
             */ /*
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long listener) {
                final long notePos = listener;
                AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
                b.setTitle("Delete A Note");
                b.setMessage("Are you sure you want to delete your "  + " note?");

                b.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    /**
                     * deletes  the note
                     * @param dialog
                     * @param listener
                     */ /*
                    public void onClick(DialogInterface dialog, int listener) {
                        databaseHelper.deleteNote(notePos);
                        Cursor newCursor = databaseHelper.getSelectAllNoteCursor();
                        cursorAdapter.changeCursor(newCursor);

                    }
                });

                b.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    /**
                     * keeps the note
                     * @param dialog
                     * @param listener
                     */ /*
                    public void onClick(DialogInterface dialog, int listener) {
                        dialog.cancel();
                    }
                });

                b.setNeutralButton("SHARE", new DialogInterface.OnClickListener() {

                    /**
                     * shares the note
                     * @param dialog
                     * @param listener
                     */ /*
                    public void onClick(DialogInterface dialog, int listener) {
                        Intent shareIntent = new Intent();
                        startActivity(shareIntent);

                    }
                });

                AlertDialog alert = b.create();
                alert.show();
                return true;
            }
        }); */


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
     * adds a note to the list
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "onActivityResult: inside");
            long id = data.getLongExtra("Id", -1);
            title = data.getStringExtra("Title");
            category = data.getStringExtra("Category");
            content = data.getStringExtra("Content");
            imageResource = data.getIntExtra("ImageResourceId", 0);
            if(id != -1) {
                Log.d(TAG, "onActivityResult: id != -1");
                databaseHelper.updateNoteById(new Note (id, title, category, content, imageResource));

            }else {
                //inset a new note

                //Note note = new Note(id, title, category, content, imageResource);
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