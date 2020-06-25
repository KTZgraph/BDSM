package com.example.loginscreen;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import at.favre.lib.crypto.bcrypt.BCrypt;


public class UserDatabase extends SQLiteOpenHelper {
    private UserData userData;



    public UserDatabase(Context context) {
        super(context, "login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO: zrobić id ale tak żeby nie dało się enrumerować użytkowników - hashe loginow np
        db.execSQL("CREATE TABLE user(username text primary key, password text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
    }

    //
    public boolean register(String rawUsername, String rawPassword) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        userData = UserData.getInstance(rawUsername); // inicjalizacja danych użytkownika
        // z tymi danymi powinna się robić nowa baza danych
        // TODO nowa baza danych notatek

        String bcryptHashString = "";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            bcryptHashString = BCrypt.withDefaults().hashToString(12, rawPassword.toCharArray()); //surowe hasło do bcrypta
            Log.i("bcryptHashString", bcryptHashString);

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("e", e.toString());
        }

        // hashe dla loginu użytkownika


        ContentValues contentValues = new ContentValues(); //NIE KLEJĘ na chama stringa do sqla <3
        contentValues.put("username", userData.getHashUsername());
        contentValues.put("password", bcryptHashString); // zaszyfrowane bcryptem

        long insertStatus = db.insert("user", null,  contentValues);

        if (insertStatus >= 0)return true;
        else return false;

    }

    public boolean checkUsername(String rawUsername) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        // sprawdzam czy użytkownik już istnieje w bazie; tylko odczyt
        SQLiteDatabase db = this.getReadableDatabase();
        userData = UserData.getInstance(rawUsername); // inicjalizacja danych użytkownika


        // =? zabezpiecza przed SQLi bo caloś traktowana jak string
        Cursor cursor = db.rawQuery("SELECT * from user where username=?", new String[] {userData.getHashUsername()});
        if (cursor.getCount()> 0) return false;
        else return  true;
    }

    //sprawdzanie loginu i hasła
    public Boolean login(String rawUsername, String rawPassword) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        SQLiteDatabase db = this.getReadableDatabase();
        userData = UserData.getInstance(rawUsername); // inicjalizacja danych użytkownika


        // =? zabezpiecza przed SQLi bo caloś traktowana jak string
        Cursor cursor = db.rawQuery("SELECT password FROM user WHERE username=?", new String[]{userData.getHashUsername()});
        Log.i("DBHelper", "Kolumny: " + cursor.getColumnNames());

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            String hashPassword = cursor.getString(cursor.getColumnIndex("password"));

            if(hashPassword.isEmpty() || rawPassword.isEmpty()) return false;

            Log.i("UserDatabase", "bcrypt sie zaczyna");
            // dokladnie to samo co https://www.codota.com/code/java/packages/at.favre.lib.crypto.bcrypt
            BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), hashPassword);
            Log.i("result", "RESULT: " + result.verified);
            if (result.verified==true) {
                System.out.println(" It matches");
                Log.i("UserDatabase",  "It matches");
                return true;
            } else {
                System.out.println(" It does not match");
                Log.i("UserDatabase",  "It does not match");
                return false;
            }
        }
        Log.i("result", "BCRYPT UMAR");
        return false;

    }

}
