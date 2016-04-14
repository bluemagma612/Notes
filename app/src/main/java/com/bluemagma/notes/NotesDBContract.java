package com.bluemagma.notes;

import android.provider.BaseColumns;

/**
 * Created by Bluemagma on 4/14/16.
 */
public class NotesDBContract {
    public static final String DATABASE_NAME = "notesdb";

    //inner class that defines the note table
    public static abstract class Note implements BaseColumns {
        public static final String TABLE_NAME = "note";
        public static final String COLUMN_NAME_NOTE_TEXT = "note_text";
        public static final String COLUMN_NAME_STATUS = "status";
        public static final String COLUMN_NAME_NOTE_DATE = "note_date";
    }
}
