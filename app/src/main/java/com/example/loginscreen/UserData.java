package com.example.loginscreen;

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
        this.hashUsername = SHA_512(rawUsername);
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

    public static String SHA_512(String originalString) throws NoSuchAlgorithmException {
        //https://developer.android.com/reference/java/security/MessageDigest
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
        byte[] result = md.digest(originalString.getBytes());
        return result.toString();
    }

}
