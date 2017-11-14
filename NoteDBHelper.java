package com.example.schwartz.pa5;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by schwartz on 11/8/17.
 */

public class NoteDBHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "databaseNotes";
    static final int DATABASE_VERSION = 1;
    // table fields
    static final String TABLE_NOTES = "tableNotes";

    static final String ID = "_id";
    static final String TITLE = "title";
    static final String CATEGORY = "category";
    static final String CONTENT = "content";
    static final String IMAGE_RESOURCE_ID = "imageResource";
    // add a tag for logcat
    static final String TAG = "NoteDatabaseHelper";

    public NoteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreate = "CREATE TABLE " + TABLE_NOTES + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TITLE + " TITLE, " +
                CATEGORY + " CATEGORY, " +
                CONTENT + " CONTENT," +
                IMAGE_RESOURCE_ID + " INTEGER)";

        Log.d(TAG, "onCreate: " + sqlCreate);
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertNote(Note note) {
        String sqlInsertNote = "INSERT INTO " + TABLE_NOTES + " VALUES(null, " + "'" + note.getTitle() + "', " + "'" + note.getCategory() + "', '" + note.getContent() + "', '" + note.getImageResource() + "')";
        Log.d(TAG, "insertNote: " + sqlInsertNote);
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sqlInsertNote);
    }

    public Cursor getSelectAllNoteCursor() {
        String sqlSelectAll = "SELECT * FROM " + TABLE_NOTES;
        //Log.d(TAG, "getSelectAllContactsCursor: " + sqlSelectAll);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sqlSelectAll, null);
        return cursor;
    }

    public List<Note> getSelectAllNoteList() {
        List<Note> contacts = new ArrayList<>();
        Cursor cursor = getSelectAllNoteCursor();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String category = cursor.getString(2);
            String content = cursor.getString(3);
            int imageResource = cursor.getInt(4);
            Note note = new Note(id,title, category, content, imageResource);
            //note.add(note);
        }

        return contacts;
    }

    public Note selectNoteById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NOTES, new String[] { ID,
                        TITLE, CATEGORY, CONTENT, IMAGE_RESOURCE_ID }, ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Note note = new Note(cursor.getLong(0),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));

        return note;
    }

    public void updateNoteById(Note note) {

        String sqlUpdateNote = "UPDATE " + TABLE_NOTES + " SET " +
                TITLE + " = '" + note.getTitle() + "', " + CATEGORY + " = '" + note.getCategory() + "', " +
                CONTENT + " = '" + note.getContent() + "', " + IMAGE_RESOURCE_ID + " = " +
                note.getImageResource() + " WHERE " + ID + " = " + note.getId();
        Log.d(TAG, "updateNote: " + sqlUpdateNote);
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sqlUpdateNote);


    }

    public void deleteNote(long id) {
        // DELETE FROM tableContacts
        //DELETE FROM tablecontacts WHERE _id = 1
       String sqlDeleteNote = "DELETE FROM " + TABLE_NOTES + " WHERE " + ID + " = " + id;
        Log.d(TAG, "deleteNote: " + sqlDeleteNote);
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sqlDeleteNote);
    }
    public void deleteAll() {
        //Open the database
        SQLiteDatabase database = this.getWritableDatabase();

        //Execute sql query to remove from database
        //NOTE: When removing by String in SQL, value must be enclosed with ''
        String sqlDeleteNotes = "DELETE FROM " + TABLE_NOTES ;
        Log.d(TAG, "insertNote: " + sqlDeleteNotes);
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sqlDeleteNotes);

        //Close the database
        database.close();
    }
}
