package com.example.loginscreen;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toolbar;

public class AddNote extends Activity {



    Toolbar toolbar;
    EditText noteTile;
    EditText noteDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("AddNote", "Przejscie do widoku");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        toolbar = findViewById(R.id.toolbar);

        noteTile = findViewById(R.id.noteTitle);
        noteDetails = findViewById(R.id.noteDetails);




    }
}
