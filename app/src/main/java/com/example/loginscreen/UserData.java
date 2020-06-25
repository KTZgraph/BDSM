package com.example.loginscreen;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserData {
    //Singleton do loginu użytkownika,
    // żeby np stworzyć baze po rejestracji i logowaniu a nie
    // ciągle przekazywac przez wszystkei klasy

    private static UserData instance;
    private String hashUsername;

    private  UserData(String rawUsername) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        this.hashUsername = SHA1(rawUsername);
        // TODO usuwać wartośc jak użytkownik się wylogowuje !!!!!!!!!!!!!!!!
    }

    public static UserData getInstance(String rawUsername) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if (instance == null){
            synchronized (UserData.class) {
                if(instance == null){ // podwojny chceck w singletonie
                    instance = new UserData(rawUsername);
                }
            }
        }
        return instance;
    }

    public String getHashUsername() {
        return hashUsername;
    }

    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String result;
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        byte[] sha1hash = md.digest();
        result = Base64.encodeToString(sha1hash, Base64.DEFAULT);
        result = result.substring(0, result.length()-1);
        return result;
    }
}
