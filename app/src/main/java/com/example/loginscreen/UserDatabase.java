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
        // TODO tutaj są dane dla nowej bazy; widok sie tym nie zajmuje
        userData = UserData.getInstance(); // inicjalizacja danych użytkownika
        userData.setHashUsername(rawUsername);
        userData.setDatabaseRawPassword(rawPassword);
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

        // usuwam dane o uzytkowniku NIEIEIEEIEIEI
//        userData.setHashUsername("");
//        userData.setDatabaseRawPassword("");

        long insertStatus = db.insert("user", null,  contentValues);

        if (insertStatus >= 0)return true;
        else return false;

    }

    // Tej metody nie robie dzisiaj juz -,-
    public boolean update(String rawOldPassword, String rawNewPassword) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        userData = UserData.getInstance(); // inicjalizacja danych użytkownika

        // sprawdzanie czy stare haslo jest dobre
        SQLiteDatabase db = this.getReadableDatabase();
        // =? zabezpiecza przed SQLi bo caloś traktowana jak string
        Cursor cursor = db.rawQuery("SELECT password FROM user WHERE username=?", new String[]{userData.getHashUsername()});
        Log.i("DBHelper", "Kolumny: " + cursor.getColumnNames());

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            String hashPassword = cursor.getString(cursor.getColumnIndex("password"));

            if(hashPassword.isEmpty() || rawOldPassword.isEmpty()) return false;

            Log.i("UserDatabase", "bcrypt sie zaczyna");
            // dokladnie to samo co https://www.codota.com/code/java/packages/at.favre.lib.crypto.bcrypt
            BCrypt.Result result = BCrypt.verifyer().verify(rawOldPassword.toCharArray(), hashPassword);
            Log.i("result", "RESULT: " + result.verified);
            if (result.verified==true) {
                System.out.println(" It matches");
                Log.i("haslo baza",  "Stare haslo pasuje");
            } else {
                System.out.println(" It does not match");
                Log.i("haslo baza",  "Stare haslo nie pasuje ");
                return false;
            }
        }else {
            Log.i("result", "BCRYPT UMAR");
            return false;
        }

        //aktualizacja nowego hasla
        db = this.getWritableDatabase();
        String newBcryptHashString = "";
        try {
            newBcryptHashString = BCrypt.withDefaults().hashToString(12, rawNewPassword.toCharArray()); //surowe hasło do bcrypta
            Log.i("bcryptHashString", newBcryptHashString);

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("e", e.toString());
        }

        // moge kleic strina i tak to tylko hash a mam zabezpieczenie przed SQLi w postaci =?
        cursor = db.rawQuery("UPDATE user SET password=? WHERE username=?", new String[]{ newBcryptHashString, userData.getHashUsername()});
        if (cursor.getCount()> 0) return false;
        else return  true;

    }


    public boolean checkUsername(String rawUsername) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        // sprawdzam czy użytkownik już istnieje w bazie; tylko odczyt
        SQLiteDatabase db = this.getReadableDatabase();
        // musze dodac najpeirw hasha do obietky // TODO !!!!!!!!!!!!!!!!!!!
        UserData userData  = UserData.getInstance(); // juz nie bedzie nulla
        userData.setHashUsername(rawUsername);


        // =? zabezpiecza przed SQLi bo caloś traktowana jak string
        Cursor cursor = db.rawQuery("SELECT * from user where username=?", new String[] {userData.getHashUsername()});
        if (cursor.getCount()> 0) return false;
        else return  true;
    }

    //sprawdzanie loginu i hasła
    public Boolean login(String rawUsername, String rawPassword) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        SQLiteDatabase db = this.getReadableDatabase();
        // TODO - tutaj zapisuje dane o userze !!!!!!!!!!!!
        userData = UserData.getInstance();
        userData.setHashUsername(rawUsername);
        userData.setDatabaseRawPassword(rawPassword); // haslo do drugiej bazy musi byc surowe
        // cos ten obiekt ma jak dane sesyjne; biedna autoryzacja



        // =? zabezpiecza przed SQLi bo caloś traktowana jak string
        Cursor cursor = db.rawQuery("SELECT password from user where username=?", new String[] {userData.getHashUsername()});

        Log.i("DBHelper", "Kolumny: " + cursor.getColumnNames());

        if (cursor != null && cursor.getCount() > 0) { //Dlaczego naglet utaj jest nullem?
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
