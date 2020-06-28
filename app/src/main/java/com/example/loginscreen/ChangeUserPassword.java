package com.example.loginscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class ChangeUserPassword extends AppCompatActivity {
    UserDatabase db;

    Toolbar toolbar;

    EditText rawOldUserpassword;
    EditText rawNewUserpassword;
    EditText rawNewUserpasswordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_password);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.heart_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edycja Hasła do logowania");

        db = new UserDatabase(this);

       rawOldUserpassword = findViewById(R.id.rawOldUserpassword); //stare haslo dla notatki
       rawNewUserpassword = findViewById(R.id.rawNewUserpassword);  // nowe haslo
       rawNewUserpasswordConfirm = findViewById(R.id.rawNewUserpasswordConfirm); // potwierdzenie nowego hasla

    }

    private void goToParentActivity() {
        Intent intentNoteActivity = new Intent(this, NoteActivity.class);
        startActivity(intentNoteActivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete){
            Toast.makeText(this, "Haslo nie zmienione", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }

        if (item.getItemId() == R.id.save){
            // dopiero tu pobieram dane
            // TODO optymalizacja na kiedys; kiedys tam
            // jak za szybko klikam to apka za duzo robi
            String oldPassword = rawOldUserpassword.getText().toString();
            String newPassword = rawNewUserpassword.getText().toString();
            String newPasswordConfirm = rawNewUserpasswordConfirm.getText().toString();

            // aktualizacja hasla uzytkownika
            if(oldPassword.isEmpty() || newPassword.isEmpty() || newPasswordConfirm.isEmpty()){
                Toast.makeText(getApplicationContext(), "Wymagane pola są puste", Toast.LENGTH_SHORT).show();
//                goToParentActivity();
            } else if(!newPassword.equals(newPasswordConfirm)){
                Toast.makeText(getApplicationContext(), "Nowe hasla sie różnią!", Toast.LENGTH_SHORT).show();
//                goToParentActivity();
            }else{
                try {
                    db.update(oldPassword, oldPassword); //TODO na nowe haslo SQLtiCipher
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Błąd podczas aktualizacji hasła", Toast.LENGTH_SHORT).show();
                }
            }


            goToParentActivity(); // powrot do rodzinca po dodaniu notatki
        }

        return super.onOptionsItemSelected(item);
    }

}
