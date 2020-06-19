package com.example.loginscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;


public class LoginActivity extends Activity {
    DatabaseHelper db;

    EditText textUsername;
    EditText textUserPassword;
    CardView cardViewLogin;
    TextView textRegisterHere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        db = new DatabaseHelper(this);


        // NA RAIZE TYLKO NOTATKI TESTUJE
        Intent noteIntent = new Intent(LoginActivity.this, NoteActivity.class);
        Log.i("Login", "Chce przejsc do widoku notatek");
        startActivity(noteIntent);

//
//        textUsername = (EditText) findViewById(R.id.textUsername);
//        textUserPassword = (EditText) findViewById(R.id.textUserPassword);
//        cardViewLogin = (CardView) findViewById(R.id.cardViewLogin); //funkcja logowania
//        cardViewLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String username = textUsername.getText().toString();
//                String rawPassword = textUserPassword.getText().toString();
//                Boolean loginStatus = db.login(username, rawPassword);
//                Log.i("LOGIN", "przed bazą danych");
//                if (loginStatus == true) {
//                    Log.i("Login", "Logged succesfully");
//                    Toast.makeText(getApplicationContext(), "Zalgowano pomyślnie", Toast.LENGTH_SHORT).show();
//                    Intent noteIntent = new Intent(LoginActivity.this, NoteActivity.class);
//                    Log.i("Login", "Chce przejsc do widoku notatek");
//                    startActivity(noteIntent);
//                }
//                else
//                    Toast.makeText(getApplicationContext(), "Nieprawidłowy login lub hasło", Toast.LENGTH_SHORT).show();
//                    Log.i("Login", "Login error for user: " + username);
//
//            }
//        });
//


        textRegisterHere = (TextView) findViewById(R.id.textRegisterHere);
        textRegisterHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, MainActivity.class); //przekierowanie do rejestracji
                Log.i("Activity", "Switch to MainActivity");
                startActivity(registerIntent);
            }
        });

    }
}
