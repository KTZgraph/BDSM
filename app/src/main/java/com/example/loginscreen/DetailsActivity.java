package com.example.loginscreen;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DetailsActivity extends AppCompatActivity {
    NoteDatabase db;
    TextView detailsOfNote;
    Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.white_arrow);
        setSupportActionBar(toolbar);

        detailsOfNote = findViewById(R.id.detailsOfNote);

        Intent intentDetailsActivity = getIntent();
        Long id = intentDetailsActivity.getLongExtra("noteID", 0);
        db = new NoteDatabase(this);
        note = db.getNote(id);

        getSupportActionBar().setTitle(note.getTitle());
        detailsOfNote.setText(note.getContent());

        Toast.makeText(this, "Title -> " + note.getTitle(), Toast.LENGTH_SHORT).show();

        FloatingActionButton fab = findViewById(R.id.deleteButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteNote(note.getID());
                startActivity(new Intent(getApplicationContext(), NoteActivity.class)); //przekirowanie do głownego activity
                Toast.makeText(getApplicationContext(), "Notatka usunięta!", Toast.LENGTH_SHORT).show();

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
            startActivity(intentEditNote);
        }


        return super.onOptionsItemSelected(item);
    }
}
