package com.example.loginscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class EditActivity extends AppCompatActivity {
    NoteDatabase db;
    Note note;

    Toolbar toolbar;
    EditText noteTitle;
    EditText noteDetails;
    Calendar calendar;
    String todaysDate;
    String currentTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intentEditNote = getIntent();
        Long id = intentEditNote.getLongExtra("noteID", 0);
        db = new NoteDatabase(this);
        note = db.getNote(id);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.heart_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(note.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noteTitle = findViewById(R.id.noteTitle);
        noteDetails = findViewById(R.id.noteDetails);

        noteTitle.setText(note.getTitle());
        noteDetails.setText(note.getContent());
        noteDetails.setMovementMethod(new ScrollingMovementMethod());

        noteTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0 || s.equals("") || s.equals(" ")){
                    getSupportActionBar().setTitle(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //get current date and time
        calendar = Calendar.getInstance();
        todaysDate = calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.YEAR);   // Dzień/Miesiąc/Rok
        currentTime = pad(calendar.get(Calendar.HOUR)) + ":" + pad(calendar.get(Calendar.MINUTE));

        Log.d("caledar", "Date and Time: " + todaysDate + "; " + currentTime);

    }

    private String pad(int i) {
        if(i<10) return "0" + i;
        else return String.valueOf(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete){
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
            onBackPressed(); // powrot do rodzinca po usunieciu notatki
        }

        if (item.getItemId() == R.id.save){
            note.setContent(noteDetails.getText().toString());
            note.setTitle(noteTitle.getText().toString());

            int id = db.editNote(note);
            if(id >= 0){
                Toast.makeText(this, "Notatka zaktualizowana", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Błąd aktualizacji!", Toast.LENGTH_SHORT).show();
            }

            // refresh danych
            Intent intentDetailsActivity = new Intent(getApplicationContext(), DetailsActivity.class);
            intentDetailsActivity.putExtra("noteID", note.getID());
            startActivity(intentDetailsActivity);
        }

        return super.onOptionsItemSelected(item);
    }

    private void goToParentActivity() {
        Intent intentNoteActivity = new Intent(this, NoteActivity.class);
        startActivity(intentNoteActivity);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}