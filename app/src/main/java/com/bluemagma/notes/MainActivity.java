package com.bluemagma.notes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private EditText mEditText;
    private RecyclerView.LayoutManager mLayoutManager;
    private NoteListItemAdapter mAdapter;
    private NoteListItem mListItem;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check to see if db connection exists
        NotesDBHelper.getInstance(this).getReadableDatabase();
        Log.i("notes", "attempting to create db");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        //display recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //add notelist items to view
        mAdapter = new NoteListItemAdapter(this, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        //create add note button
        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text in the EditText
                EditText editText = (EditText) findViewById(R.id.edit_text);

                // Create a new NoteListItem with the text
                mListItem = new NoteListItem(editText.getText().toString());

                // Add the item to the adapter
                mAdapter.addItem(mListItem);

                //add to database
                NoteDAO dao = new NoteDAO(MainActivity.this);
                dao.save(mListItem);

                // Set the EditText to an empty string
                editText.setText("");

                //scroll up
                mLayoutManager.scrollToPosition(0);
            }
        });
        //animate adding new item to view
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //get database entries
        NotesDBHelper.getInstance(this).getReadableDatabase();

        //set the background color of a note
        setColor();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Toast toast = Toast.makeText(getApplicationContext(), "Settings clicked",
                    Toast.LENGTH_SHORT);
            toast.show();
            openColorDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            if (data.hasExtra("Note")) {
                NoteListItem note = (NoteListItem) data.getSerializableExtra("Note");
                Toast.makeText(this, note.getText(), Toast.LENGTH_LONG).show();
                mAdapter.addItem(note);
            }
            //if (data.hasExtra("Color")) {
                //setColor();
            //}
        }
    }

    public void openColorDialog(){
        final EditText input = new EditText(this);

        new AlertDialog.Builder(this)
                .setTitle(R.string.setting_color_title)
                .setMessage(R.string.setting_color_message)
                .setView(input)
                .setPositiveButton(R.string.positive_button_label, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //get value
                        String value = input.getText().toString();

                        //save as a preference
                        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("NOTE_COLOR", value);
                        editor.commit();

                        setColor();
                    }
                }).setNegativeButton(R.string.negative_button_label, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                }).show();
    }

    public void setColor() {
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        if (prefs.getString("NOTE_COLOR", "W") != null) {
            String color = prefs.getString("NOTE_COLOR", "W");
            if (color.toUpperCase().contains("G")) {
                mRecyclerView.setBackgroundColor(Color.GREEN);
            } else if (color.toUpperCase().contains("R")) {
                mRecyclerView.setBackgroundColor(Color.RED);
            } else {
                mRecyclerView.setBackgroundColor(Color.WHITE);
            }
        }
    }
}
