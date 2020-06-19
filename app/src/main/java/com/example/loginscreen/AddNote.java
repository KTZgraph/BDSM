package com.example.loginscreen;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;


public class AddNote extends AppCompatActivity {
    Toolbar toolbar;
    EditText noteTile;
    EditText noteDetails;
    Calendar calendar;
    String todaysDate;
    String currentTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("AddNote", "Przejscie do widoku dodawania notatek");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Note");

        noteTile = findViewById(R.id.noteTitle);
        noteDetails = findViewById(R.id.noteDetails);

        noteTile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0){
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
}
