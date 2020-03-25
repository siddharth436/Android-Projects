package com.example.multinotes;

import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private List<Notes> notesList;
    private MainActivity mainAct;

    public NotesAdapter(List<Notes> notesList, MainActivity mainAct) {
        this.notesList = notesList;
        this.mainAct = mainAct;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notes_layout, viewGroup, false);
        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        Notes note = notesList.get(position);
        myViewHolder.title.setText(note.getTitle());
        myViewHolder.timestamp.setText(note.getTimestamp());
        if(note.getNote().length() > 80){
            String new_Note = note.getNote().substring(0, 79);
            new_Note = new_Note.concat("...");
            myViewHolder.note.setText(new_Note);
        }
        else {
            myViewHolder.note.setText(note.getNote());
        }
    }

    @Override
    public int getItemCount() {
        mainAct.getSupportActionBar().setTitle("Notes "+"("+notesList.size()+")");
        return notesList.size();
    }
}
