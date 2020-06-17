package com.example.loginscreen;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

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
                String password = textUserPassword.getText().toString();
                String passwordConfirmation = textUserPasswordConfirmation.getText().toString();

                if(username.equals("") || password.equals("") || passwordConfirmation.equals("")){
                    Toast.makeText(getApplicationContext(), "Wymagane pola są puste", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (password.equals(passwordConfirmation)){
//                        Boolean checkUsername = db.checkUsername(username);
                        if(db.checkUsername(username) == true){
                            //prawidłowa rejestracja
                            // TODO:  dodac hashowanie hasła użytkownika tutaj
                            Boolean insertStatus = db.insert(username, password);
                            if (insertStatus == true){
                                Toast.makeText(getApplicationContext(), "Rejestracja powiodła się", Toast.LENGTH_SHORT).show();

                            }
                        }else{
                            //nieprawidłowa rejestracja
                            Toast.makeText(getApplicationContext(), "Nazwa użytkownika jest zajęta", Toast.LENGTH_SHORT).show();
                        }
                    }
                    Toast.makeText(getApplicationContext(), "Hasło się różnią", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // przekierowanie do logowania
        textLoginHere = (TextView) findViewById(R.id.textLoginHere);
        textLoginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(registerIntent);
            }
        });

    }
}
