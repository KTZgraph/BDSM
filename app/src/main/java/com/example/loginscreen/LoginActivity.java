package com.example.loginscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

public class LoginActivity extends Activity {

    EditText textUsername;
    EditText textUserPassword;
    CardView cardLogin;
    TextView registerHere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textUsername = (EditText) findViewById(R.id.textUsername);
        textUserPassword = (EditText) findViewById(R.id.textUserPassword);
        cardLogin = (CardView) findViewById(R.id.cardLogin);
        registerHere = (TextView) findViewById(R.id.registerHere);
        registerHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

    }
}
