package com.bluemagma.notes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditNoteActivity extends AppCompatActivity {

    private EditText noteEditText;
    private NoteListItem note;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        Intent intent = getIntent();
        note = (NoteListItem)intent.getSerializableExtra("Note");

        noteEditText = (EditText)findViewById(R.id.note_text_edit);
        noteEditText.setText(note.getText());

        saveButton = (Button)findViewById(R.id.note_save_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String note_text = noteEditText.getText().toString();
                note.setText(note_text);
                NoteDAO dao = new NoteDAO(EditNoteActivity.this);
                dao.update(note);
                Intent intent = new Intent();
                intent.putExtra("Note", note);
                setResult(RESULT_OK, intent);
                finish();
                Log.i("Notes", "Save button saving completed");
            }

        });
    }

}
