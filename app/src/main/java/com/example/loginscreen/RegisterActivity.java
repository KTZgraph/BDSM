package com.example.loginscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.cardview.widget.CardView;

public class RegisterActivity extends Activity {
    EditText textUsername;
    EditText textUserPassword;
    EditText textUserPasswordConfirmation;
    CardView cardRegister;
    TextView textLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        textUsername = (EditText) findViewById(R.id.textUsername);
        textUserPassword = (EditText) findViewById(R.id.textUserPassword);
        textUserPasswordConfirmation = (EditText) findViewById(R.id.textUserPasswordConfirmation);
        cardRegister = (CardView) findViewById(R.id.cardLogin);

        textLogin = (TextView) findViewById(R.id.textLogin);
        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(registerIntent);
            }
        });

    }
}
