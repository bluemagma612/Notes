package com.bluemagma.notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Bluemagma on 4/14/16.
 */
public class NoteDAO {
    private Context context;

    public NoteDAO(Context context){
        this.context = context;
    }

    public void save(NoteListItem note){
        NotesDBHelper helper = NotesDBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NotesDBContract.Note.COLUMN_NAME_NOTE_TEXT, note.getText());
        values.put(NotesDBContract.Note.COLUMN_NAME_STATUS, note.getStatus());
        values.put(NotesDBContract.Note.COLUMN_NAME_NOTE_DATE, (note.getDate().getTimeInMillis()/1000));
        db.insert(NotesDBContract.Note.TABLE_NAME, null, values);
    }

    public void update(NoteListItem noteListItem){
        NotesDBHelper helper = NotesDBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NotesDBContract.Note.COLUMN_NAME_NOTE_TEXT, noteListItem.getText());

        String selection = NotesDBContract.Note.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = { String.valueOf(noteListItem.getId()) };

        int count = db.update(
                NotesDBContract.Note.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public void delete(NoteListItem noteListItem){
        String selection = NotesDBContract.Note.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = { String.valueOf(noteListItem.getId()) };
        NotesDBHelper helper = NotesDBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        db.delete(NotesDBContract.Note.TABLE_NAME, selection, selectionArgs);
        Log.i("Notes","note deleted " + noteListItem.getText());

    }

    public ArrayList<NoteListItem> list(){
        NotesDBHelper helper = NotesDBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projection = {
                NotesDBContract.Note.COLUMN_NAME_ID,
                NotesDBContract.Note.COLUMN_NAME_NOTE_TEXT,
                NotesDBContract.Note.COLUMN_NAME_STATUS,
                NotesDBContract.Note.COLUMN_NAME_NOTE_DATE
        };
        String sortOrder = NotesDBContract.Note.COLUMN_NAME_NOTE_DATE + " DESC";

        Cursor c = db.query(
                NotesDBContract.Note.TABLE_NAME,        // The table to query
                projection,                             // The columns to return
                null,                                   // The columns for the WHERE clause
                null,                                   // The values for the WHERE clause
                null,                                   // don't group the rows
                null,                                   // don't filter by row groups
                sortOrder                               // The sort order
        );

        ArrayList<NoteListItem> notes = new ArrayList<NoteListItem>();

        while(c.moveToNext()){
            long id = c.getLong(c.getColumnIndex(NotesDBContract.Note.COLUMN_NAME_ID));
            String text = c.getString(c.getColumnIndex(NotesDBContract.Note.COLUMN_NAME_NOTE_TEXT));
            String status = c.getString(c.getColumnIndex(NotesDBContract.Note.COLUMN_NAME_STATUS));
            Calendar date = new GregorianCalendar();
            date.setTimeInMillis(c.getLong(c.getColumnIndex(NotesDBContract.Note.COLUMN_NAME_NOTE_DATE)) * 1000);
            notes.add(new NoteListItem(id, text, status, date));
        }

        return notes;
    }
}