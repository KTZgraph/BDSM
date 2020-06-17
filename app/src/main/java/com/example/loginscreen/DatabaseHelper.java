package com.example.loginscreen;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user(username text primary key, password text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
    }

    //
    public boolean insert(String username, String password){
        //NIE KLEJĘ na chama stringa do sqla <3
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);

        long insertStatus = db.insert("user", null,  contentValues);
//        if (insertStatus == 1) return false; else return  true;
        if (insertStatus == 1){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean checkUsername(String username){
        // sprawdzam cyz użytkownik już istnieje w bazie; tylko odczyt
        SQLiteDatabase db = this.getReadableDatabase();
        // TODO: zmienic z raw query na coś bezpiecznsiejszego
        Cursor cursor = db.rawQuery("SELECT * from user where username=?", new String[] {username});
        if (cursor.getCount()> 0) return false;
        else return  true;
    }
}
