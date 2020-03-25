package com.example.multinotes;

import android.content.DialogInterface;
import android.content.Intent;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.Calendar;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;



public class EditActivity extends AppCompatActivity {

    private EditText editTitle;
    private EditText editContent;
    private Menu mOptionsMenu;
    private String getPreviousTitle = "" , getPreviousContent = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        editTitle = findViewById(R.id.edit_title);
        editContent = findViewById(R.id.edit_note);

        Intent intent = getIntent();
        if (intent.hasExtra("TITLE")) {
            getPreviousTitle = intent.getStringExtra("TITLE");
            editTitle.setText(getPreviousTitle);
        }
        if (intent.hasExtra("NOTE")) {
            getPreviousContent = intent.getStringExtra("NOTE");
            editContent.setText(getPreviousContent);
        }

        editContent.setMovementMethod(new ScrollingMovementMethod());
        editContent.setGravity(Gravity.TOP);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1a237e")));
    }

    @Override
    public void onBackPressed() {
        if(editTitle.getText().toString().isEmpty()){
            Toast.makeText(this, "Note was not saved", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }
        else if(getPreviousTitle.equals(editTitle.getText().toString()) && getPreviousContent.equals(editContent.getText().toString())){
            super.onBackPressed();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    saveNote();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });
            builder.setMessage("Do you want to save this Content?");
            builder.setTitle("Note Save");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        mOptionsMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void saveNote(){
        Notes newNote = new Notes();
        newNote.setTitle(editTitle.getText().toString());
        newNote.setNote(editContent.getText().toString());
        newNote.setTimestamp(Calendar.getInstance().getTime().toString());
        Intent resultIntent = new Intent();
        resultIntent.putExtra("EDIT_NOTE", newNote);
        if(editTitle.getText().toString().isEmpty()) {
            resultIntent.putExtra("STATUS", "NO_CHANGE");
            Toast.makeText(this, "Empty Note Cannot Be Saved", Toast.LENGTH_SHORT).show();
        }
        else if(getPreviousTitle.isEmpty() && getPreviousContent.isEmpty())
            resultIntent.putExtra("STATUS", "NEW");
        else if(getPreviousTitle.equals(editTitle.getText().toString()) && getPreviousContent.equals(editContent.getText().toString()))
            resultIntent.putExtra("STATUS", "NO_CHANGE");
        else
            resultIntent.putExtra("STATUS", "CHANGE");
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
