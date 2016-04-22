package com.bluemagma.notes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class NoteListItemAdapter extends RecyclerView.Adapter<NoteListItemAdapter.ViewHolder> {
    private Context mContext;
    private RecyclerView mRecyclerView;
    private ArrayList<NoteListItem> mNoteListItems = new ArrayList<NoteListItem>();

    public void addItem(NoteListItem item) {
        mNoteListItems.add(0, item);
        notifyItemInserted(0);
    }

    public void removeItem(int position) {
        mNoteListItems.remove(position);
        notifyItemRemoved(position);
    }

    public NoteListItemAdapter(Context context, RecyclerView recyclerView) {
        this.mContext = context;
        this.mRecyclerView = recyclerView;

        NoteDAO dao = new NoteDAO(context);
        mNoteListItems = dao.list();
    }

    @Override
    public NoteListItemAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.note_list_item, viewGroup, false);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mPosition = mRecyclerView.getChildAdapterPosition(v);
                Log.i("Notes","clicked note at position " + mPosition);

                //remove from database
                Log.i("Notes", "note list item position to be deleted: " + mPosition);
                NoteListItem noteListItem = mNoteListItems.get(mPosition);

                NoteDAO dao = new NoteDAO(mContext);

                Log.i("Notes", "starting delete of: " + noteListItem.getText());
                dao.delete(noteListItem);

                //remove from ui
                Toast.makeText(mContext, "Deleted: " + noteListItem.getText(), Toast.LENGTH_LONG).show();
                removeItem(mPosition);
            }
        });

        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v){
                NoteListItem noteListItem = mNoteListItems.get(mRecyclerView.getChildAdapterPosition(v));
                removeItem(mRecyclerView.getChildAdapterPosition(v));
                Intent intent = new Intent(mContext, EditNoteActivity.class);
                intent.putExtra("Note", noteListItem);
                ((Activity)mContext).startActivityForResult(intent, 1);

                return true;
            }
        });
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NoteListItemAdapter.ViewHolder viewHolder, int i) {
        NoteListItem noteListItem = mNoteListItems.get(i);
        viewHolder.setText(noteListItem.getText());
    }

    @Override
    public int getItemCount() {
        return mNoteListItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
        }

        public void setText(String text) {
            this.text.setText(text);
        }

    }


}

