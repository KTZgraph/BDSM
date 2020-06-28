package com.example.loginscreen;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;


public class DetailsActivity extends AppCompatActivity {
    NoteDatabase db;
    TextView detailsOfNote;
    Note note;
    private String noteRawPassword; // musze przekazac do edycji

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        SQLiteDatabase.loadLibs(this); // TODO


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.heart_back);
        setSupportActionBar(toolbar);

        detailsOfNote = findViewById(R.id.detailsOfNote);

        Intent intentDetailsActivity = getIntent();
        Long id = intentDetailsActivity.getLongExtra("noteID", 0);
        this.noteRawPassword = intentDetailsActivity.getStringExtra("noteRawPassword");

        try {
            db = NoteDatabase.getInstance(DetailsActivity.this, ""); //tu baza jest juz zrobiona
            note = db.getNote(id);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "[Exception] pobierania notatki", Toast.LENGTH_SHORT).show();
        }

        getSupportActionBar().setTitle(note.getDate());
        String notePlaintText = note.getPlainText(this.noteRawPassword); //zrobione
        if (notePlaintText == null){
            Toast.makeText(this, "BLAD Podano nieprawidlowe haslo dla notatki", Toast.LENGTH_SHORT).show();
            // jak zle haslo to powrot do listy notatek
            //musi zostac bo tak jest tylko widok szczegolow notatki i nie wiadomo o co chodzi
            startActivity(new Intent(getApplicationContext(), NoteActivity.class)); //przekirowanie do głownego activity
        }
        detailsOfNote.setText(notePlaintText);

        Toast.makeText(this, "DetailsActivity -> ", Toast.LENGTH_SHORT).show(); //TODO
        FloatingActionButton fab = findViewById(R.id.deleteButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    db.deleteNote(note.getID());
                    startActivity(new Intent(getApplicationContext(), NoteActivity.class)); //przekirowanie do głownego activity
                    Toast.makeText(getApplicationContext(), "Notatka usunięta!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Bład usuwania notatki db.deleteNote(note.getID())!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.editNote) {
            // send user to edit activity
            Toast.makeText(this, "Edit note", Toast.LENGTH_SHORT).show();
            Intent intentEditNote = new Intent(this, EditActivity.class);
            intentEditNote.putExtra("noteID", note.getID());
            intentEditNote.putExtra("rawPassword",this.noteRawPassword);
            this.noteRawPassword = null; //zerowanie hasla po podaniu do nowego widoku
            startActivity(intentEditNote);
        }


        return super.onOptionsItemSelected(item);
    }
}
