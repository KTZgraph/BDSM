package com.example.loginscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
        db = new DatabaseHelper(this);

        textUsername = (EditText) findViewById(R.id.textUsername);
        textUserPassword = (EditText) findViewById(R.id.textUserPassword);
        cardViewLogin = (CardView) findViewById(R.id.cardViewLogin); //funkcja logowania

        textRegisterHere = (TextView) findViewById(R.id.textRegisterHere);
        textRegisterHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, MainActivity.class); //przekierowanie do rejestracji
                startActivity(registerIntent);
            }
        });

    }
}
