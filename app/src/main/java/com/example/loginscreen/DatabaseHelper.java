package com.example.loginscreen;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import at.favre.lib.crypto.bcrypt.BCrypt;


public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO: zrobić id ale tak żeby nie dało się enrumerować użytkowników
        db.execSQL("CREATE TABLE user(username text primary key, password text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
    }

    //
    public boolean register(String username, String rawPassword){

        SQLiteDatabase db = this.getWritableDatabase();
        String bcryptHashString = BCrypt.withDefaults().hashToString(12, rawPassword.toCharArray()); //surowe hasło do bcrypta

        ContentValues contentValues = new ContentValues(); //NIE KLEJĘ na chama stringa do sqla <3
        contentValues.put("username", username);
        contentValues.put("password", bcryptHashString); // zaszyfrowane bcryptem

        long insertStatus = db.insert("user", null,  contentValues);

        if (insertStatus == 1)return false;
        else return false;

    }

    public boolean checkUsername(String username){
        // sprawdzam czy użytkownik już istnieje w bazie; tylko odczyt
        SQLiteDatabase db = this.getReadableDatabase();
        // TODO: zmienic z raw query na coś bezpiecznsiejszego, bo ktoś zrobić śrendick czy coś i będzie problem
        Cursor cursor = db.rawQuery("SELECT * from user where username=?", new String[] {username});
        if (cursor.getCount()> 0) return false;
        else return  true;
    }

    //sprawdzanie loginu i hasła
    public Boolean login(String username, String rawPassword) {
        SQLiteDatabase db = this.getReadableDatabase();
        String hashPassword = "";

        // TODO: zmienic z raw query na coś bezpiecznsiejszego, bo ktoś zrobić śrendick czy coś i będzie problem
        Cursor cursor = db.rawQuery("SELECT password FROM user WHERE username=?", new String[]{username});
        Log.i("DBHelper", "Kolumny: " + cursor.getColumnNames());

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            hashPassword = cursor.getString(cursor.getColumnIndex("password"));
            Log.i("DatabaseHelper", "Get user password");
        }

        try {
            Log.i("DatabaseHelper", "bcrypt sie zaczyna");

            // dokladnie to samo co https://www.codota.com/code/java/packages/at.favre.lib.crypto.bcrypt
            BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), hashPassword);
            if (result.verified) {
                System.out.println(" It matches");
                Log.i("DatabaseHelper",  "It matches");

                return true;
            } else {
                System.out.println(" It does not match");
                Log.i("DatabaseHelper",  "It does not match");

                return false;
            }

        }catch (Exception e){
            Log.i("Database helper", "hashe porownywanie sie zestralo");
        }

        return false;
    }

}
