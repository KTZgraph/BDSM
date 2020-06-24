package com.example.loginscreen;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;


// ----------------- zamiana importów na umożliwienie szyfrowania bazy danych
import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;
import net.sqlcipher.database.SQLiteOpenHelper;


import java.util.ArrayList;
import java.util.List;



public class NoteDatabase extends SQLiteOpenHelper {
    private static NoteDatabase instance; //Singleton
    private static SQLiteDatabaseHook hook = new SQLiteDatabaseHook(){
        public void preKey(SQLiteDatabase database){
            database.rawExecSQL("PRAGMA kdf_iter = 64000;");
//            database.rawExecSQL("PRAGMA cipher_hmac_algorithm = HMAC_SHA1;");
//            database.rawExecSQL("PRAGMA cipher_kdf_algorithm = PBKDF2_HMAC_SHA1;");
//            database.rawExecSQL("PRAGMA cipher = ‘aes-256-cfb’;"); // nie można zmieniac na CTR ani nawet na OFB :<
        }
        public void postKey(SQLiteDatabase database){}
    };

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "notes2.db";
    private static final String DATABASE_TABLE = "note";

    // columns name for database table
    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_CIPHERTEXT = "ciphertext";
    private static final String KEY_SECRET = "secret";
    private static final String KEY_SALT = "salt";
    private static final String KEY_IV = "iv";

    public static final String PASS_PHARSE = "!@#ABC"; // hasło dla bazy

    static public synchronized NoteDatabase getInstance(Context context){
        if(instance == null){
            instance = new NoteDatabase(context);
        }return instance;
    }

    public NoteDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION, hook);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //CREATE table nametable(id NT PRMARY KEY, title TEXT, content TEXT , date TEXT, time TEXT);
        String query = "CREATE TABLE " + DATABASE_TABLE +
                "(" +
                    KEY_ID          + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    KEY_TIME        + " TEXT, " +
                    KEY_DATE        + " TEXT, " +
                    KEY_CIPHERTEXT  + " TEXT, " +
                    KEY_SECRET      + " TEXT, " +
                    KEY_SALT        + " TEXT, " +
                    KEY_IV          + " TEXT " +
                ")";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion >= newVersion)
            return;
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);

    }

    public long addNote(Note note){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHARSE);

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TIME, note.getTime());
        contentValues.put(KEY_DATE, note.getDate());
        contentValues.put(KEY_CIPHERTEXT, note.getCiphertext());
        contentValues.put(KEY_SECRET, note.getSecret().toString());
        contentValues.put(KEY_SALT, note.getSalt());
        contentValues.put(KEY_IV, note.getIv());

        long ID = db.insert(DATABASE_TABLE, null, contentValues);
        Log.d("NoteDatabase", "ID -> " + ID);
        db.close();
        return ID;
    }

    public Note getNote(long id){
        // ROZSZYFROWUJĘ DOPIERO W ACTIVITY
        //pobieranie pojedynczej notatki
        // "=?" zapobiega SQLInjection
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHARSE);
        String query = "SELECT * from ";
        Cursor cursor = db.query(DATABASE_TABLE,
                new String[]{
                        KEY_ID,
                        KEY_TIME,
                        KEY_DATE,
                        KEY_CIPHERTEXT,
                        KEY_SECRET,
                        KEY_SALT,
                        KEY_IV
                        }, KEY_ID + "=?",

                new String[]{String.valueOf(id)}, null, null, null);

        if(cursor != null)
            cursor.moveToFirst(); //bo cursos zaczyna się zawsze od -1

        Note note = new Note();
        note.setID(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
        note.setTime(cursor.getString(cursor.getColumnIndex(KEY_TIME)));
        note.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));

        // trewsc notatki
        note.setCiphertext(cursor.getString(cursor.getColumnIndex(KEY_CIPHERTEXT)));

        // hasło aes
        note.setSecret(cursor.getString(cursor.getColumnIndex(KEY_SECRET)));

        // sól
        note.setSalt(cursor.getBlob(cursor.getColumnIndex(KEY_SALT)));

        // wektor inicjalizujacy
        note.setIv(cursor.getBlob(cursor.getColumnIndex(KEY_IV)));

        db.close();
        return note;
    }

    public List<Note> getAllNotes(){
        SQLiteDatabase db = instance.getReadableDatabase(PASS_PHARSE);
        List<Note> allNotes = new ArrayList<>();
        // wybieranie wszystkich danych z bazy

        String query = "SELECT * FROM " + DATABASE_TABLE + " ORDER BY " + KEY_ID + " DESC";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor != null && cursor.moveToFirst()){
            do{
                Note note = new Note();
                note.setID(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
                note.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
                note.setTime(cursor.getString(cursor.getColumnIndex(KEY_TIME)));

                // trewsc notatki
                note.setCiphertext(cursor.getString(cursor.getColumnIndex(KEY_CIPHERTEXT)));

                // hasło aes
                note.setSecret(cursor.getString(cursor.getColumnIndex(KEY_SECRET)));

                // sól
//                note.setSalt(cursor.getString(cursor.getColumnIndex(KEY_SALT)));
                byte[] salt = cursor.getBlob(cursor.getColumnIndex(KEY_SALT));
                note.setSalt(salt);

                byte[] iv = cursor.getBlob(cursor.getColumnIndex(KEY_IV));
                note.setIv(iv);

                allNotes.add(note);
            }while(cursor.moveToNext());
        }

        db.close();
        return allNotes;
    }

    public int editNote(Note note){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHARSE);
        ContentValues contentValues = new ContentValues();
        Log.d("EDITED DB", "Edited password -> " + note.getSecret() + "\n ID ->" + note.getID());
        contentValues.put(KEY_DATE, note.getDate());
        contentValues.put(KEY_TIME, note.getTime());

        contentValues.put(KEY_CIPHERTEXT, note.getCiphertext());
        contentValues.put(KEY_SECRET, note.getSecret());
        contentValues.put(KEY_SALT, note.getSalt());
        contentValues.put(KEY_IV, note.getIv());

        int editValue = db.update(DATABASE_TABLE, contentValues, KEY_ID+"=?", new String[]{String.valueOf(note.getID())});
        db.close();
        return editValue;
    }


    void deleteNote(long id){
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHARSE);
        db.delete(DATABASE_TABLE, KEY_ID+"=?", new String[] {String.valueOf(id)});
        db.close();
    }


}
