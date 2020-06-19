package com.example.loginscreen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

public class NoteActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Notatki
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note); //widok rejestracji uzytkownika
        Log.i("NoteActivity", "przejscie do widoku notatek");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.listOfNotes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("onCreateOptionsMenu", "onCreateOptionsMenu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("onOptionsItemSelected", "item");

        if (item.getItemId() == R.id.add){
            Toast.makeText(this, "ADD btn is Clicked", Toast.LENGTH_SHORT).show();
            Intent addNoteIntent = new Intent(this, AddNote.class);
            startActivity(addNoteIntent);

            Log.i("onOptionsItemSelected", "item.getItemId()");

        }
        return super.onOptionsItemSelected(item);
    }
}
