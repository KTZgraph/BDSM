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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.List;

public class NoteActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    Adapter adapter;
    List<Note> allNotes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        SQLiteDatabase.loadLibs(this);

        Log.i("NoteActivity", "przejscie do widoku notatek");
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.heart_back); // TODO TODO TODO
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Wszystkie Notatki");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        NoteDatabase db = null; // TODO tutaj problemy

        try {
            db = NoteDatabase.getInstance(NoteActivity.this);
            Log.i("NoteDatabase db", "db: " + db);

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("BAza", "db = NoteDatabase.getInstance(NoteActivity.this)");
            Log.i("BAza", e.toString());
        } // TODo wyjątki

        allNotes = db.getAllNotes();

        recyclerView = findViewById(R.id.listOfNotes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, allNotes);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
