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

import net.sqlcipher.database.SQLiteDatabase;

import java.util.Calendar;

public class EditActivity extends AppCompatActivity {
    NoteDatabase db;
    Note note;

    Toolbar toolbar;
    EditText noteDetails;
    Calendar calendar;
    String todaysDate;
    String currentTime;
    EditText oldRawNotePassword; //stare haslo dla notatki
    EditText noteRawNewPassword; // nowe haslo
    EditText noteRawNewPasswordConfirm; // potwierdzenie nowego hasla



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        SQLiteDatabase.loadLibs(this); // TODO


        Intent intentEditNote = getIntent();
        Long id = intentEditNote.getLongExtra("noteID", 0);
        // do tego widoku i tak nie da się przejsc bez wczesniejszego rozszyfrowania i podania prawidlowego hasla
        String passwordFromDetailsAcitivity = intentEditNote.getStringExtra("rawPassword");

        try {
            db =  NoteDatabase.getInstance(EditActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        note = db.getNote(id);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.heart_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edycja");


        oldRawNotePassword = findViewById(R.id.oldRawNotePassword);
        noteRawNewPassword = findViewById(R.id.noteRawNewPassword);
        noteRawNewPasswordConfirm = findViewById(R.id.noteRawNewPasswordConfirm);
        noteDetails = findViewById(R.id.rawNoteContent);


        //  ------------------------ UPDATE NOTATKI ------------------------
        // zrobione
        // ROZSZYFROWANIE NOTATKI na potrzeby widoku jak bedzie zle haslo to tu nawet nie wjedzie
        noteDetails.setText(note.getPlainText(passwordFromDetailsAcitivity)); //surowe haslo od uzytkownika
        noteDetails.setMovementMethod(new ScrollingMovementMethod());

        oldRawNotePassword.addTextChangedListener(new TextWatcher() {
            // EDYCJA HASLA
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0 || s.equals("") || s.equals(" ")){
                    getSupportActionBar().setTitle("Edytowanie hasla dla tej notatki");
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
            // AKTUALIZACJA NOTATKI
            String rawNewConente = noteDetails.getText().toString();
            try {
                String newPassword = noteRawNewPassword.getText().toString();
                String newPasswordConfirmation = noteRawNewPasswordConfirm.getText().toString();
                String oldPassword = oldRawNotePassword.getText().toString();
                if(newPassword.equals(newPasswordConfirmation)){
                    note.update(newPassword, oldPassword, rawNewConente);
                }else{
                    Toast.makeText(this, "Błąd aktualizacji! Nowe hasła róznią się!", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Edit activity", "Note update error");
            }

            int id = db.editNote(note);
            if(id >= 0){
                Toast.makeText(this, "Notatka zaktualizowana", Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(this, "Błąd aktualizacji!", Toast.LENGTH_SHORT).show();
                Intent intentDetailsActivity = new Intent(getApplicationContext(), DetailsActivity.class);
                intentDetailsActivity.putExtra("noteID", note.getID());
                startActivity(intentDetailsActivity);
            }

            goToParentActivity(); // zamaist refreszu po prostu wracam do listy dnotatek
            // bo inaczej user i tak musiałby wpisac hasło na nowo
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
