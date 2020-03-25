package com.example.multinotes;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.util.JsonWriter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{

    private static final int REQUEST_CODE = 100;
    private List<Notes> NotesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;
    private int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recycler);
        notesAdapter = new NotesAdapter(NotesList, this);
        recyclerView.setAdapter(notesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        new MyAsyncTask(this).execute();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1a237e")));
    }

    @Override
    public void onClick(View v) {

        position = recyclerView.getChildLayoutPosition(v);
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        intent.putExtra("TITLE", NotesList.get(position).getTitle());
        intent.putExtra("DATE", NotesList.get(position).getTimestamp());
        intent.putExtra("NOTE", NotesList.get(position).getNote());
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public boolean onLongClick(View v) {  // long click listener called by ViewHolder long clicks
        position = recyclerView.getChildLayoutPosition(v);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                NotesList.remove(position);
                notesAdapter.notifyDataSetChanged();
                position = -1;
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                position = -1;
            }
        });
        builder.setMessage("Are you sure you want to delete this note?");
        builder.setTitle("Delete");
        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
    }

    @Override
    protected void onResume(){
        NotesList.size();
        super.onResume();
        notesAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {

        //Save the NotesList to the Json file
        saveNotes();
        super.onPause();
    }

    private void saveNotes() {

        try {
            FileOutputStream fos = getApplicationContext().openFileOutput("Notes.json", Context.MODE_PRIVATE);

            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8));
            writer.setIndent("  ");
            writer.beginObject();
            writer.name("notes");
            writeNotesArray(writer);
            writer.endObject();
            writer.close();

//            Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void writeNotesArray(JsonWriter writer) throws IOException {
        writer.beginArray();
        for (Notes value : NotesList) {
            writeNotesObject(writer, value);
        }
        writer.endArray();
    }

    public void writeNotesObject(JsonWriter writer, Notes val) throws IOException{
        writer.beginObject();
        writer.name("title").value(val.getTitle());
        writer.name("timestamp").value(val.getTimestamp());
        writer.name("note").value(val.getNote());
        writer.endObject();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.info:

                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.add:

                Intent intent1 = new Intent(MainActivity.this, EditActivity.class);
                startActivityForResult(intent1, REQUEST_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Notes edit_note = (Notes) data.getExtras().getSerializable("EDIT_NOTE");
                String status = data.getStringExtra("STATUS");
                if (status.equals("NO_CHANGE")) {
                } else if (status.equals("CHANGE")) {
                    NotesList.remove(position);
                    NotesList.add(0, edit_note);
                } else if (status.equals("NEW")) {
                    NotesList.add(0, edit_note);
                }
            }
        }
    }

    public List<Notes> getNotesList() {
        return NotesList;
    }

}