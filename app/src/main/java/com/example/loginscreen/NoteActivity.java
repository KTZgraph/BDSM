package com.example.loginscreen;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;


import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

public class NoteActivity extends Activity {
    Toolbar toolbar;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Notatki
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note); //widok rejestracji uzytkownika
        Log.i("NoteActivity", "przejscie do widoku notatek");
        toolbar = findViewById(R.id.toolbar);

        recyclerView = findViewById(R.id.listOfNotes);

    }

}
