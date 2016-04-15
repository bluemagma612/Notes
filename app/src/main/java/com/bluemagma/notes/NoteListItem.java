package com.bluemagma.notes;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Bluemagma on 3/22/16.
 */
public class NoteListItem implements Serializable {
    private long id;
    private String text;
    private String status;
    private Calendar date;

    public NoteListItem(String text) {
        this.text = text;
        this.status = status;
        this.date = date;
    }

    public NoteListItem(String text, String status, Calendar date){
        this.text = text;
        this.status = status;
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }
}
