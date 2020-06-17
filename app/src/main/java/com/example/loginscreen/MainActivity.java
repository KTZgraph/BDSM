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

import at.favre.lib.crypto.bcrypt.BCrypt;

public class MainActivity extends Activity {
    DatabaseHelper db;

    EditText textUsername;
    EditText textUserPassword;
    EditText textUserPasswordConfirmation;
    CardView cardViewRegister;
    TextView textLoginHere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // REJESTRACJA
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //widok rejestracji uzytkownika
        db = new DatabaseHelper(this);


        textUsername = (EditText) findViewById(R.id.textUsername);
        textUserPassword = (EditText) findViewById(R.id.textUserPassword);
        textUserPasswordConfirmation = (EditText) findViewById(R.id.textUserPasswordConfirmation);


        cardViewRegister = (CardView) findViewById(R.id.cardViewRegister);
        cardViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = textUsername.getText().toString();
                String rawPassword = textUserPassword.getText().toString();
                String rawPasswordConfirmation = textUserPasswordConfirmation.getText().toString();

                if(username.isEmpty() || rawPassword.isEmpty() || rawPasswordConfirmation.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Wymagane pola są puste", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (rawPassword.equals(rawPasswordConfirmation)){ // podane hasła identyczne sprawdzam same stringi nie hashe
                        if(db.checkUsername(username) == true){ // login wolny
                            // TODO wymuśić mocne hasla na użytkowniku
                            Boolean registerStatus = db.register(username, rawPassword);
                            if (registerStatus == true){
                                Toast.makeText(getApplicationContext(), "Rejestracja powiodła się", Toast.LENGTH_SHORT).show();
                                Log.i("Register", "Registered successfully");
                            }
                        }else{
                            //nieprawidłowa rejestracja
                            Toast.makeText(getApplicationContext(), "Nazwa użytkownika jest zajęta", Toast.LENGTH_SHORT).show();
                            Log.i("Register", "Error user not registered");

                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Hasła się różnią", Toast.LENGTH_SHORT).show();
                        Log.i("Register", "Passwords don't match");

                    }
                }
            }
        });


        // przekierowanie do logowania
        textLoginHere = (TextView) findViewById(R.id.textLoginHere);
        textLoginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });

    }

}
