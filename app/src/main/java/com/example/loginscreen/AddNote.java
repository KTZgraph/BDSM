package com.example.loginscreen;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.Calendar;


public class AddNote extends AppCompatActivity {
    Toolbar toolbar;
    EditText rawPassword;
    EditText noteContent;
    Calendar calendar;
    String todaysDate;
    String currentTime;


    String rawTmpPassword = "Bar12345Bar12345";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("AddNote", "Przejscie do widoku dodawania notatek");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        SQLiteDatabase.loadLibs(this); // TODO


        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.heart_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Nowa Notatka");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        rawPassword = findViewById(R.id.notePassword);
        noteContent = findViewById(R.id.noteContent);

        noteContent.addTextChangedListener(new TextWatcher() {
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
            // Tworzenie nowej notatki
            Note note;
            try {
//                note = new Note(rawPassword.getText().toString(), noteContent.getText().toString(), todaysDate, currentTime);
                note = new Note(rawTmpPassword, noteContent.getText().toString(), todaysDate, currentTime);
                NoteDatabase db = NoteDatabase.getInstance(AddNote.this);
                db.addNote(note);
                Toast.makeText(this, "Notatka zapisana", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Błąd - notatka nie zapisana", Toast.LENGTH_SHORT).show();
            }

            goToParentActivity(); // powrot do rodzinca po dodaniu notatki
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
