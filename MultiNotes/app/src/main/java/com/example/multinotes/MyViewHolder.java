package com.example.multinotes;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class MyViewHolder extends RecyclerView.ViewHolder{


    public TextView title;
    public TextView note;
    public TextView timestamp;

    public MyViewHolder(@NonNull View view) {
        super(view);

       note = view.findViewById(R.id.notes);
       title = view.findViewById(R.id.title);
       timestamp = view.findViewById(R.id.timestamp);
    }
}
