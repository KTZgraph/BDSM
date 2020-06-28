package com.example.loginscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
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
    String oldRawNotePassword; //stare haslo dla notatki
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
        this.oldRawNotePassword = intentEditNote.getStringExtra("rawPassword");

        try {
            db =  NoteDatabase.getInstance(EditActivity.this, ""); //tu baza juz istnieje
            note = db.getNote(id);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, " NoteDatabase.getInstance(EditActivity.this)!", Toast.LENGTH_LONG).show();

        }

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.heart_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edycja");


        noteRawNewPassword = findViewById(R.id.rawNewUserpassword);
        noteRawNewPasswordConfirm = findViewById(R.id.rawNewUserpasswordConfirm);
        noteDetails = findViewById(R.id.rawNoteContent);


        //  ------------------------ UPDATE NOTATKI ------------------------
        // zrobione
        // ROZSZYFROWANIE NOTATKI na potrzeby widoku jak bedzie zle haslo to tu nawet nie wjedzie
        noteDetails.setText(note.getPlainText(this.oldRawNotePassword)); //surowe haslo od uzytkownika
        noteDetails.setMovementMethod(new ScrollingMovementMethod());


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
                if(newPassword.equals(newPasswordConfirmation)){
                    if(PasswordValidator.valid(newPassword)){
                        note.update(newPassword, this.oldRawNotePassword, rawNewConente);
                    }else{
                        Toast.makeText(this, "Błąd aktualizacji! Slabe haslo!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "Błąd aktualizacji! Nowe hasła róznią się!", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Edit activity", "Note update error");
            }

            int id = 0;
            try {
                id = db.editNote(note);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Błąd db.editNote(note)!", Toast.LENGTH_SHORT).show();
            }


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
